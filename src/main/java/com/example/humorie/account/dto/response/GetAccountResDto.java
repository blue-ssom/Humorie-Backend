package com.example.humorie.account.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetAccountResDto {

    private Long id;

    // 아이디
    private String accountName;

    // 이메일
    private String email;

    // 이메일 수신
    private Boolean emailSubscription;


}
