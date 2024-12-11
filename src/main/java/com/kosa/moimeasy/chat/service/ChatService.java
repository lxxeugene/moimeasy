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
    public ChatRoom createRoom(CreateRoomDTO request, Long userId) {
        // 요청한 사용자가 포함된 `moeimId`로 멤버 필터링
        List<User> members = userRepository.findAllById(request.getMemberIds())
                .stream()
                .filter(user -> user.getMoeimId().equals(request.getCreatedBy()))
                .collect(Collectors.toList());

        if (members.isEmpty()) {
            throw new IllegalArgumentException("해당 멤버는 모임에 속해 있지 않습니다.");
        }

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setUsers(members);

        // 채팅방 이름 설정
        if (request.getRoomName() != null && !request.getRoomName().isEmpty()) {
            chatRoom.setName(request.getRoomName());
        } else {
            chatRoom.setName(generateDefaultRoomName(members));
        }

        chatRoom.setCreatedBy(userId);
        return chatRoomRepository.save(chatRoom);
    }

    private String generateDefaultRoomName(List<User> members) {
        return members.stream()
                .map(User::getNickname)
                .limit(3)
                .collect(Collectors.joining(", ")) +
                (members.size() > 3 ? " 외 " + (members.size() - 3) + "명" : "");
    }

    public List<ChatRoom> getAllRooms(Long userId) {
        return chatRoomRepository.findByUserId(userId);
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

//    public List<ChatRoom> getAllRooms() {
//        return chatRoomRepository.findAll();
//    }

    public List<ChatMessage> getMessagesSince(Long roomId, Long lastMessageId) {
        return chatMessageRepository.findByChatRoomIdAndIdGreaterThan(roomId, lastMessageId);
    }

}




