package com.kosa.moimeasy.Moeim;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoeimDTO {
    private Long moeimid;         
    private Long userId;     
    private String moeimName; 
    private String moeimCode; 
}
