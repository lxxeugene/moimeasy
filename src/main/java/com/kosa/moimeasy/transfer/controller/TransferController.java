package com.kosa.moimeasy.transfer.controller;

import com.kosa.moimeasy.transfer.dto.TransferRequestDTO;
import com.kosa.moimeasy.transfer.entity.TransferHistory;
import com.kosa.moimeasy.transfer.service.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/transfer", produces = MediaType.APPLICATION_JSON_VALUE) // 모든 응답이 JSON 형식
public class TransferController {

    private final TransferService transferService;

    // 거래내역 조회
    @GetMapping("")
    public List<TransferHistory> getTransferHistory(){
        return transferService.getTransferHistory();
    }

    // 계좌 이체
    @PostMapping("/pay")
    public ResponseEntity<TransferRequestDTO> pay(@RequestBody TransferRequestDTO transferRequestDTO){
        transferService.executeTransfer(transferRequestDTO);
        return ResponseEntity.ok().build();
    }
}
