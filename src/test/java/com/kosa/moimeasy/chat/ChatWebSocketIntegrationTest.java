package com.kosa.moimeasy.chat;

import com.kosa.moimeasy.chat.dto.SendMessageDTO;
import com.kosa.moimeasy.chat.entity.ChatMessage;
import com.kosa.moimeasy.chat.entity.ChatMessage.MessageType;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ChatWebSocketIntegrationTest {

    @LocalServerPort
    private int port;

    private WebSocketStompClient stompClient;
    private StompSession stompSession;

    private final static String WS_ENDPOINT = "/ws-chat";
    private final static String SEND_DEST = "/app/chat.send";
    private final static String SUBSCRIBE_DEST = "/topic/room.1";

    private final static int TIMEOUT = 3;

    @BeforeEach
    public void setup() throws Exception {
        List<Transport> transports = List.of(new WebSocketTransport(new StandardWebSocketClient()));
        SockJsClient sockJsClient = new SockJsClient(transports);

        stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        String url = "ws://localhost:" + port + WS_ENDPOINT;
        ListenableFuture<StompSession> future = stompClient.connect(url, new StompSessionHandlerAdapter() {});
        stompSession = future.get(TIMEOUT, TimeUnit.SECONDS);
    }

    @AfterEach
    public void teardown() {
        if (stompSession != null && stompSession.isConnected()) {
            stompSession.disconnect();
        }
    }

    @Test
    public void 채팅메시지_전송_및_수신_테스트() throws Exception {
        CompletableFuture<ChatMessage> completableFuture = new CompletableFuture<>();

        stompSession.subscribe(SUBSCRIBE_DEST, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatMessage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                completableFuture.complete((ChatMessage) payload);
            }
        });

        // 테스트용 메시지 전송
        SendMessageDTO dto = new SendMessageDTO();
        dto.setChatRoomId(1L); // 반드시 존재하는 채팅방 ID로 테스트
        dto.setSenderId(1L);    // 존재하는 사용자 ID
        dto.setMessageType(MessageType.TEXT.name());
        dto.setContent("테스트 메시지");

        stompSession.send(SEND_DEST, dto);

        // 응답 수신 확인
        ChatMessage received = completableFuture.get(TIMEOUT, TimeUnit.SECONDS);

        assertThat(received.getContent()).isEqualTo("테스트 메시지");
        assertThat(received.getChatRoom().getId()).isEqualTo(1L);
    }
}
