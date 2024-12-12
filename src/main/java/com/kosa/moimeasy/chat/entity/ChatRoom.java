package com.kosa.moimeasy.chat.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import com.kosa.moimeasy.user.entity.User;

@Entity
@Table(name = "chat_rooms")
@Getter
@Setter
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 채팅방 이름

    @ManyToMany
    @JoinTable(
        name = "chat_room_members",
        joinColumns = @JoinColumn(name = "chat_room_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users = new ArrayList<>(); // 채팅방 사용자 목록

    @Column(nullable = false)
    private Long createdBy; // 채팅방 생성자
}



