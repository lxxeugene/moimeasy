package com.kosa.moimeasy.transfer.service;

import com.kosa.moimeasy.moeim.dto.MoeimDTO;
import com.kosa.moimeasy.moeim.entity.Moeim;
import com.kosa.moimeasy.moeim.repository.MoeimRepository;
import com.kosa.moimeasy.moeim.service.MoeimService;
import com.kosa.moimeasy.user.entity.User;
import com.kosa.moimeasy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WithdrawalService {

    private MoeimRepository moeimRepository;
    private UserRepository userRepository;

    // 모임 계좌에서 출금
    @Transactional
    public void moeimAccountWithdraw(String moeimAccount, BigDecimal amount){

        Moeim withdrawAccount = moeimRepository.findAccountNumber(moeimAccount);

        // 출금 후 계좌 잔액 업데이트
        BigDecimal withdrawResult = withdrawAccount.getBalance().subtract(amount);
        withdrawAccount.setBalance(withdrawResult);
    }

    // 회원 계좌에서 출금
    @Transactional
    public void userAccountWithdraw(String userAccount, BigDecimal amount){

        User withdrawAccount = userRepository.findAccountNumber(userAccount);

        // 출금 후 계좌 잔액 업데이트
        BigDecimal withdrawResult = withdrawAccount.getBalance().subtract(amount);
        withdrawAccount.setBalance(withdrawResult);
    }

}
