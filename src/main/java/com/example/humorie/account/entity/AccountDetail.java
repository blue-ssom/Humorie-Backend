package com.example.humorie.account.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String email;

    //id: 고유 식별자, accountName: 아이디 값
    private String accountName;

    private String password;

    private String name;

    private String phoneNumber;

    private LocalDate joinDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoginType loginType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountRole role;

    public static AccountDetail joinAccount(String email, String encodedPassword, String accountName, String phoneNumber, String name) {
        AccountDetail accountDetail = new AccountDetail();
        accountDetail.email = email;
        accountDetail.password = encodedPassword;
        accountDetail.accountName = accountName;
        accountDetail.phoneNumber = phoneNumber;
        accountDetail.role = AccountRole.USER;
        accountDetail.loginType = LoginType.JWT;
        accountDetail.joinDate = LocalDate.now();

        return accountDetail;
    }



}
