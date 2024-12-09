package com.kosa.moimeasy.moeim.service;

import com.kosa.moimeasy.invitation.entity.Invitation;
import com.kosa.moimeasy.invitation.repository.InvitationRepository;
import com.kosa.moimeasy.moeim.dto.MoeimDTO;
import com.kosa.moimeasy.moeim.entity.Moeim;
import com.kosa.moimeasy.moeim.repository.MoeimRepository;
import com.kosa.moimeasy.user.entity.User;
import com.kosa.moimeasy.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Random;

@Service
public class MoeimService {

    @Autowired
    private MoeimRepository moeimRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    public Moeim createMoeim(MoeimDTO request) {
        // 1. 사용자 조회
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 2. 모임 생성
        Moeim moeim = new Moeim();
        moeim.setMoeimName(request.getMoeimName());
        moeim.setUser(user); // User 객체를 설정
        moeim.setAccountNum(generateVirtualAccountNumber());
        moeim.setMoeimCode(generateMoeimCode());
        Moeim savedMoeim = moeimRepository.save(moeim);

        // 3. 사용자 업데이트 (moeimId, role 변경)
        user.setMoeimId(savedMoeim.getMoeimId());
        user.setRole(user.getRole()); // Enum 값을 설정 /
        userRepository.save(user);

        return savedMoeim;
    }


    // 가상 계좌 번호 생성 메서드
    private String generateVirtualAccountNumber() {
        Random random = new Random();
        StringBuilder accountNum = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            accountNum.append(random.nextInt(10));
        }
        return accountNum.toString();
    }

    // 모임 코드 생성 메서드 (6자리 숫자)
    private String generateMoeimCode() {
        Random random = new Random();
        StringBuilder moeimCode = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            moeimCode.append(random.nextInt(10));
        }
        return moeimCode.toString();
    }

    public void joinMoeim(MoeimDTO request) {
        // 1. 로그인한 사용자 가져오기
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 2. 초대 이메일 검증
        Invitation invitation = invitationRepository.findFirstByEmailOrderByCreatedAtDesc(user.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("초대된 이메일이 아닙니다."));

        // 3. 초대된 모임 ID로 모임 테이블에서 데이터 가져오기
        Moeim moeim = moeimRepository.findById(invitation.getMoeimId())
                .orElseThrow(() -> new IllegalArgumentException("초대받은 모임이 존재하지 않습니다."));

        // 4. 사용자가 입력한 모임 코드와 데이터베이스의 모임 코드 비교
        if (!moeim.getMoeimCode().equals(request.getMoeimCode())) {
            throw new IllegalArgumentException("입력된 모임 코드가 초대받은 모임 코드와 일치하지 않습니다.");
        }

        // 5. 사용자 모임 ID 업데이트
        user.setMoeimId(moeim.getMoeimId());
        userRepository.save(user);

        // 6. 초대 상태를 COMPLETED로 업데이트
        invitation.setStatus(Invitation.InvitationStatus.COMPLETED);
        invitationRepository.save(invitation);
    }


}
