package com.kosa.moimeasy.transaction.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InitialDataDto { // 초기 데이터

    private String userName;
    private String userAccount;
    private String moeimName;
    private String moeimAccount;
    private List<CategoryDto> categories; // 카테고리별 데이터 리스트

}