package com.kosa.moimeasy.chat.repository;

import com.kosa.moimeasy.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}

