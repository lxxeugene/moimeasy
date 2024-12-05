package com.kosa.moimeasy.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kosa.moimeasy.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

//     @GetMapping
// public ResponseEntity<List<UserDTO>> getAllUsers() {
//     List<User> users = userService.findAllUsers(); // 서비스에서 모든 사용자 조회
//     List<UserDTO> userDTOs = users.stream()
//             .map(UserDTO::new) // User를 UserDTO로 변환
//             .toList();
//     return ResponseEntity.ok(userDTOs);
// }

@GetMapping
public ResponseEntity<List<UserDTO>> getUsersByMoeimId(@RequestParam Long moeimId) {
    List<User> users = userService.findByMoeimId(moeimId); // 특정 모임 ID로 사용자 조회
    List<UserDTO> userDTOs = users.stream()
            .map(UserDTO::new)
            .toList();
    return ResponseEntity.ok(userDTOs);
}


    @PostMapping("/create")
    public User createUser(@RequestBody UserDTO request) {
        return userService.createUser(request);
    }

    @PutMapping("/update/{userId}")
    public User updateUser(@PathVariable Long userId, @RequestBody UserDTO request) {
        return userService.updateUser(userId, request);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        
        User user = userService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        
        
        UserDTO userDTO = new UserDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return "사용자가 삭제되었습니다.";
    }
}
