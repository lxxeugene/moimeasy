package com.kosa.moimeasy.chat.repository;

import com.kosa.moimeasy.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomIdAndIdGreaterThan(Long chatRoomId, Long id);
}

