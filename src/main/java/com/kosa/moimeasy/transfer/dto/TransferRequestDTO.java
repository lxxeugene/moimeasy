package com.kosa.moimeasy.transfer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class TransferRequestDTO {
    private Long userId; // 회원 id
    private Long moeimId; // 모임 id
    private BigDecimal transferAmount; // 이체 금액
    private String memo; // 메모
    private String transferType; // 이체 타입
}
