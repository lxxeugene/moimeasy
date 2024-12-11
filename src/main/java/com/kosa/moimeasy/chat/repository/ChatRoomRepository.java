package com.kosa.moimeasy.chat.repository;

import com.kosa.moimeasy.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @EntityGraph(attributePaths = {"users"})
    List<ChatRoom> findAll();
}
