package com.kosa.moimeasy.chat.repository;

import com.kosa.moimeasy.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
