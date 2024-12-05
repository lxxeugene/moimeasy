package com.kosa.moimeasy.chat.service;

import com.kosa.moimeasy.chat.entity.ChatRoom;
import com.kosa.moimeasy.chat.entity.ChatMessage;
import com.kosa.moimeasy.chat.entity.ChatMessage.MessageType;
import com.kosa.moimeasy.chat.repository.ChatRoomRepository;
import com.kosa.moimeasy.chat.repository.ChatMessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ChatService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public ChatRoom createRoom(String roomName, ChatRoom.RoomType roomType) {
        ChatRoom chatRoom = ChatRoom.builder()
                .roomName(roomName)
                .roomType(roomType)
                .createdAt(LocalDateTime.now())
                .build();
        return chatRoomRepository.save(chatRoom);
    }

    @Transactional
    public ChatMessage sendMessage(Long chatRoomId, Long senderId, String content, MessageType messageType) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다."));

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .senderId(senderId)
                .content(content)
                .messageType(messageType)
                .createdAt(LocalDateTime.now())
                .build();

        return chatMessageRepository.save(chatMessage);
    }

    @Transactional
    public void updateReadStatus(Long messageId) {
        ChatMessage chatMessage = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("해당 메시지가 존재하지 않습니다."));

        chatMessage.setReadCount(chatMessage.getReadCount() + 1);
        chatMessageRepository.save(chatMessage);
    }
}

