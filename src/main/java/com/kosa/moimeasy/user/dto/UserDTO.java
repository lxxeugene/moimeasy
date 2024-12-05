package com.kosa.moimeasy.user.dto;

import java.time.LocalDateTime;

import com.kosa.moimeasy.user.entity.User;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long userId;         
    private String userName; 
    private String password; 
    private String address;  
    private String email;    
    private String phone;    
    private String nickname; 
    private LocalDateTime createAt;
    private Long moeimId;    
    private String role;     

    public UserDTO(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.nickname = user.getNickname();
        this.address = user.getAddress();
        this.createAt = user.getCreateAt();
        this.moeimId = user.getMoeimId();
        this.role = user.getRole() != null ? user.getRole().name() : null;
    }
}

