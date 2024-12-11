package com.kosa.moimeasy.transfer.service;

import com.kosa.moimeasy.moeim.entity.Moeim;
import com.kosa.moimeasy.moeim.repository.MoeimRepository;
import com.kosa.moimeasy.transfer.dto.TransferRequestDTO;
import com.kosa.moimeasy.transfer.entity.TransferHistory;
import com.kosa.moimeasy.transfer.repository.CategoryRepository;
import com.kosa.moimeasy.transfer.repository.TransferHistoryRepository;
import com.kosa.moimeasy.user.entity.User;
import com.kosa.moimeasy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {

    private final UserRepository userRepository;
    private final MoeimRepository moeimRepository;
    private final TransferHistoryRepository transferHistoryRepository;
    private final CategoryRepository categoryRepository;

    // 이체 실행
    public void executeTransfer(TransferRequestDTO transferRequestDTO){
        Long userId = transferRequestDTO.getUserId(); // 회원 아이디
        Long moeimId = transferRequestDTO.getMoeimId(); // 모임 아이디

        User userAccount = getUserAccount(userId); // 회원 계좌
        Moeim moeimAccount = getMoeimAccount(moeimId); // 모임 계좌

        userAccount.withdraw(transferRequestDTO.getTransferAmount()); // 회원 계좌 출금
        moeimAccount.deposit(transferRequestDTO.getTransferAmount()); // 모임 계좌 입금

        saveTransferHistory(transferRequestDTO, userAccount.getBalance(), moeimAccount.getBalance());
    }

    // 거래내역 조회
    public List<TransferHistory> getTransferHistory(){
        return transferHistoryRepository.findAll();
    }

    // 회원 계좌에서 출금, 모임 계좌에 입금 내역 기록
    private void saveTransferHistory(TransferRequestDTO transferRequestDTO, BigDecimal amountAfterWithdrawal, BigDecimal amountAfterDeposit){
        TransferHistory transferHistory = new TransferHistory(
                getUserAccount(transferRequestDTO.getUserId()),
                amountAfterWithdrawal,
                transferRequestDTO.getMemo(),
                getMoeimAccount(transferRequestDTO.getMoeimId()),
                amountAfterDeposit,
                transferRequestDTO.getTransferAmount(),
                TransferHistory.TransferType.INCOME
        );
        transferHistoryRepository.save(transferHistory); // 거래내역 저장
    }

    // 모임 계좌 조회
    private Moeim getMoeimAccount(Long moeimId){
        return moeimRepository.findAccountNumberById(moeimId)
                .orElseThrow(IllegalArgumentException::new);
    }

    // 회원 계좌 조회
    private User getUserAccount(Long userId){
        return userRepository.findAccountNumberById(userId)
                .orElseThrow(IllegalArgumentException::new);
    }

}
