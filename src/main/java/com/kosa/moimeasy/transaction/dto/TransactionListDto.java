package com.kosa.moimeasy.transaction.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

public class TransactionListDto {

    @Getter
    public static class Request {

        @NotNull
        @Min(1)
        private Long accountId;             // 거래 내역을 조회할 계좌 id

        @NotBlank(message = "계좌번호는 필수값입니다.")
        private String accountNumber;       // 거래 내역의 조회할 계좌번호

        @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate startDate;    // 조회 시작 날짜

        @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate endDate;      // 조회 마지막 날짜
    }

    @Getter
    @Builder
    public static class Response {

        private List<TransactionDto> transactionList;
    }
}
