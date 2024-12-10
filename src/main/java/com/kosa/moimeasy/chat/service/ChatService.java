package com.kosa.moimeasy.chat.service;

import com.kosa.moimeasy.chat.entity.ChatRoom;
import com.kosa.moimeasy.chat.dto.CreateRoomDTO;
import com.kosa.moimeasy.chat.dto.SendMessageDTO;
import com.kosa.moimeasy.chat.entity.ChatMessage;
import com.kosa.moimeasy.chat.entity.ChatMessage.MessageType;
import com.kosa.moimeasy.chat.repository.ChatRoomRepository;
import com.kosa.moimeasy.user.entity.User;
import com.kosa.moimeasy.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import com.kosa.moimeasy.chat.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    @Transactional
    public ChatRoom createRoom(CreateRoomDTO request) {
        ChatRoom chatRoom = new ChatRoom();

        // 선택된 회원 가져오기
        List<User> members = userRepository.findAllById(request.getMemberIds());
        chatRoom.setUsers(members);

        // 채팅방 이름 설정 로직
        if (request.getRoomName() != null && !request.getRoomName().isEmpty()) {
            chatRoom.setName(request.getRoomName()); // 사용자 지정 이름
        } else {
            chatRoom.setName(generateDefaultRoomName(members)); // 자동 생성 이름
        }

        chatRoom.setCreatedBy(request.getCreatedBy());
        return chatRoomRepository.save(chatRoom);
    }

    private String generateDefaultRoomName(List<User> members) {
        if (members.size() <= 2) {
            return members.stream()
                          .map(User::getNickname)
                          .collect(Collectors.joining(", "));
        }
        return members.stream()
                      .limit(3)
                      .map(User::getNickname)
                      .collect(Collectors.joining(", ")) + " 외 " + (members.size() - 3) + "명";
    }

    @Transactional
    public ChatMessage sendMessage(SendMessageDTO request) {
        ChatRoom chatRoom = chatRoomRepository.findById(request.getChatRoomId())
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        ChatMessage message = new ChatMessage();
        message.setSender(request.getSenderId());
        message.setChatRoom(chatRoom);
        message.setMessageType(MessageType.valueOf(request.getMessageType().toUpperCase()));

        if (request.getMessageType().equalsIgnoreCase("TEXT")) {
            message.setContent(request.getContent());
        } else {
            message.setFileUrl(request.getFileUrl()); // 파일 경로 저장
        }

        return chatMessageRepository.save(message);
    }

    public List<ChatRoom> getAllRooms() {
        return chatRoomRepository.findAll();
    }
}




