package com.kosa.moimeasy.chat;

import com.kosa.moimeasy.chat.dto.SendMessageDTO;
import com.kosa.moimeasy.chat.entity.ChatMessage;
import com.kosa.moimeasy.chat.entity.ChatRoom;
import com.kosa.moimeasy.chat.entity.ChatRoomUser;
import com.kosa.moimeasy.chat.repository.ChatMessageRepository;
import com.kosa.moimeasy.chat.repository.ChatRoomRepository;
import com.kosa.moimeasy.chat.repository.ChatRoomUserRepository;
import com.kosa.moimeasy.moeim.entity.Moeim;
import com.kosa.moimeasy.moeim.repository.MoeimRepository;
import com.kosa.moimeasy.user.entity.Role;
import com.kosa.moimeasy.user.entity.User;
import com.kosa.moimeasy.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.*;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class ChatWebSocketIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired private UserRepository userRepository;
    @Autowired private MoeimRepository moeimRepository;
    @Autowired private ChatRoomRepository chatRoomRepository;
    @Autowired private ChatRoomUserRepository chatRoomUserRepository;
    @Autowired private ChatMessageRepository chatMessageRepository;

    private WebSocketStompClient stompClient;
    private StompSession stompSession;

    private Long testUserId;
    private Long testRoomId;

    @PersistenceContext
    private EntityManager entityManager;

    private static final String WS_ENDPOINT = "/ws-chat";
    private static final String SEND_DEST = "/app/chat.send";
    private static final String TOPIC_PREFIX = "/topic/room.";

    private Role findOrCreateRole(String roleName) {
        // role_name이 이미 존재하는지 직접 쿼리
        List<Role> existingRoles = entityManager.createQuery(
                        "SELECT r FROM Role r WHERE r.roleName = :roleName", Role.class)
                .setParameter("roleName", roleName)
                .getResultList();

        if (!existingRoles.isEmpty()) {
            return existingRoles.get(0);
        }

        // 없으면 새로 저장
        Role role = new Role();
        role.setRoleName(roleName);
        entityManager.persist(role); // 이 부분은 트랜잭션 안에서 실행되어야 함
        return role;
    }

    @BeforeEach
    @Transactional
    public void setup() throws Exception {
        // 1. Role
        Role role = findOrCreateRole("USER");

        // 2. 사용자 생성 및 저장
        User user = new User();
        user.setUserName("테스트유저");
        user.setEmail("t@example.com");
        user.setPassword("password");
        user.setPhone("01012345678");
        user.setNickname("테스트닉");
        user.setAccountNumber("1234567890");
        user.setAmount(10000.0);
        user.setRole(role);
        user = userRepository.save(user);
        testUserId = user.getUserId();

        // 3. 채팅방 생성 (ChatRoom 먼저 만들어야 Moeim에 set 가능)
        ChatRoom room = new ChatRoom();
        room.setName("테스트방");
        room.setCreatedBy(testUserId);
        room = chatRoomRepository.save(room);
        testRoomId = room.getId();

        // 4. 모임(Moeim) 생성 및 ChatRoom과 연관관계 설정
        Moeim moeim = new Moeim();
        moeim.setUser(user);
        moeim.setMoeimName("테스트모임");
        moeim.setAccountNumber("1234567890");
        moeim.setAmount(0.0);
        moeim.setChatRoom(room); // 💡 ChatRoom 객체 주입
        moeim = moeimRepository.save(moeim);

        // 5. ChatRoom에 Moeim 연결 (양방향이라면 setMoeim 필요)
        room.setMoeim(moeim); // 💡 다시 연결 (양방향 관계라면 필요)
        chatRoomRepository.save(room); // update

        // 6. 채팅방 참여자 등록
        ChatRoomUser cru = new ChatRoomUser();
        cru.setChatRoom(room);
        cru.setUser(user);
        cru.setUserNickname(user.getNickname());
        chatRoomUserRepository.save(cru);

        // 7. 웹소켓 연결
        stompClient = new WebSocketStompClient(
                new SockJsClient(List.of(new WebSocketTransport(new StandardWebSocketClient())))
        );
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        String url = "ws://localhost:" + port + WS_ENDPOINT;
        stompSession = stompClient.connect(url, new StompSessionHandlerAdapter() {}).get(3, TimeUnit.SECONDS);
    }


    @Test
    public void 채팅_테스트() throws Exception {
        CompletableFuture<ChatMessage> future = new CompletableFuture<>();

        stompSession.subscribe(TOPIC_PREFIX + testRoomId, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatMessage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                future.complete((ChatMessage) payload);
            }
        });

        // 메시지 전송
        SendMessageDTO dto = new SendMessageDTO();
        dto.setChatRoomId(testRoomId);
        dto.setSenderId(testUserId);
        dto.setMessageType("TEXT");
        dto.setContent("WebSocket 통합테스트 메시지");

        stompSession.send(SEND_DEST, dto);

        // 결과 검증
        ChatMessage result = future.get(3, TimeUnit.SECONDS);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo("WebSocket 통합테스트 메시지");
        assertThat(result.getChatRoom().getId()).isEqualTo(testRoomId);
    }

    @AfterEach
    @Transactional
    public void tearDown() {
        chatMessageRepository.deleteAll();
        chatRoomUserRepository.deleteAll();
        chatRoomRepository.deleteAll();
        moeimRepository.deleteAll();
        userRepository.deleteAll();
    }
}