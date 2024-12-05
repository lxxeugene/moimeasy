package com.kosa.moimeasy.moeim.controller;

import com.kosa.moimeasy.moeim.dto.MoeimDTO;
import com.kosa.moimeasy.moeim.service.MoeimService;
import com.kosa.moimeasy.moeim.entity.Moeim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/moeim")
@CrossOrigin(origins = "http://localhost:3000")
public class MoeimController {

    @Autowired
    private MoeimService moeimService;

    @PostMapping("/create")
    public ResponseEntity<Moeim> createMoeim(@RequestBody MoeimDTO request) {
        Long userId = 10L; // 로그인된 사용자 ID를 실제 인증 정보를 기반으로 가져오세요
        request.setUserId(userId);

        Moeim createdMoeim = moeimService.createMoeim(request);
        return ResponseEntity.ok(createdMoeim);
    }

    @PostMapping("/join")
public ResponseEntity<String> joinMoeim(@RequestBody MoeimDTO request) {
    Long userId = 9L; // 로그인된 사용자 ID로 설정
    request.setUserId(userId);

    try {
        moeimService.joinMoeim(request);
        return ResponseEntity.ok("모임 가입이 완료되었습니다.");
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

}
