package com.kosa.moimeasy.transaction.controller;

import com.kosa.moimeasy.transaction.dto.DepositDto;
import com.kosa.moimeasy.transaction.dto.RemittanceDto;
import com.kosa.moimeasy.transaction.dto.TransactionListDto;
import com.kosa.moimeasy.transaction.dto.WithdrawDto;
import com.kosa.moimeasy.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    // 입금
    @PutMapping("/deposit")
    public ResponseEntity<DepositDto.Response> deposit(
            @RequestBody @Valid DepositDto.Request request
    ) {
        DepositDto.Response response = transactionService.deposit(request);
        return ResponseEntity.ok(response);
    }

    // 출금
    @PutMapping("/withdraw")
    public ResponseEntity<WithdrawDto.Response> withdraw(
            @RequestBody @Valid WithdrawDto.Request request
    ) {
        // 사용자 정보를 SecurityContextHolder에서 가져옵니다.
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        WithdrawDto.Response response = transactionService.withdraw(username, request);
        return ResponseEntity.ok(response);
    }


    // 송금
    @PutMapping("/remittance")
    public ResponseEntity<RemittanceDto.Response> remittance(
            @RequestBody @Valid RemittanceDto.Request request
    ) {
        // 사용자 정보를 SecurityContextHolder에서 가져옵니다.
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(transactionService.remittance(username, request));
    }

    /*
     * @return 최소 하루 ~ 최대 일주일의 거래 내역
     * @apiNote 시작 날짜, 끝 날짜 둘 다 null 로 보낼 경우 조회일 포함 일주일 내역 반환
     * @apiNote 날짜 포맷 : yyyy-MM-dd
     */
    // 거래내역
    @GetMapping("/transaction-list")
    public ResponseEntity<TransactionListDto.Response> getTransactionList(
            @RequestBody @Valid TransactionListDto.Request request
    ) {
        // 사용자 정보를 SecurityContextHolder에서 가져옵니다.
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(transactionService.getTransactionList(username, request));
    }
}
