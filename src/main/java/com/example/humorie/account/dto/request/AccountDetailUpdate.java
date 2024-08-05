package com.example.humorie.account.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDetailUpdate {
    private String name;
    private String email;
    private String password;
    private String passwordCheck;
    private Boolean emailSubscription;
}
