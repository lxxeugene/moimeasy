package com.kosa.moimeasy.user.service;

import com.kosa.moimeasy.security.repository.MemberRepository;
import com.kosa.moimeasy.user.dto.PasswordResetRequest;
import com.kosa.moimeasy.user.dto.PasswordResetResponse;
import com.kosa.moimeasy.user.entity.User;
import com.kosa.moimeasy.user.exception.InvalidPasswordResetException;
import com.kosa.moimeasy.user.exception.MemberNotFoundException;
import com.kosa.moimeasy.user.exception.PasswordMismatchException;
import com.kosa.moimeasy.user.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FindServiceImpl implements FindService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public String findEmailByNicknameAndPhone(String nickname, String phone) throws UserNotFoundException {
        // 닉네임과 연락처로 회원 찾기
        User user =  memberRepository.findByNicknameAndPhone(nickname, phone)
                .orElseThrow(() -> new UserNotFoundException("일치하는 회원을 찾을 수 없습니다."));
        return user.getEmail();
    }

    @Override
    @Transactional
    public boolean findUserByNicknameAndPhoneAndEmail(String nickname, String phone, String email) throws UserNotFoundException {
        // 닉네임, 연락처, 이메일로 회원 찾기
        Optional<User> user = memberRepository.findByNicknameAndPhoneAndEmail(nickname, phone, email);
        return user.isPresent();
    }

    @Override
    @Transactional
    public PasswordResetResponse resetPassword(PasswordResetRequest request)
            throws MemberNotFoundException, PasswordMismatchException, InvalidPasswordResetException {

        List<String> errors = new ArrayList<>();

        // 닉네임으로 회원 찾기
        Optional<User> memberOpt = memberRepository.findByNicknameAndPhoneAndEmail(request.getNickname(), request.getPhone(), request.getEmail());
        if (memberOpt.isEmpty()) {
            errors.add("일치하는 회원을 찾을 수 없습니다.");
        }

        if (!errors.isEmpty()) {
            throw new InvalidPasswordResetException(errors);
        }

        // 비밀번호 일치 확인
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new PasswordMismatchException("새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        User user = memberRepository.findByNicknameAndPhoneAndEmail(
                request.getNickname(),
                request.getPhone(),
                request.getEmail()
        ).orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다."));

        // 비밀번호 인코딩 후 설정
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        memberRepository.save(user);

        return new PasswordResetResponse("비밀번호가 성공적으로 변경되었습니다.");
    }
}
