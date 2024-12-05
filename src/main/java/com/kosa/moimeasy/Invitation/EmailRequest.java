package com.kosa.moimeasy.Invitation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequest {
    private String email;
    private String message; 
    private String htmlContent;
}
