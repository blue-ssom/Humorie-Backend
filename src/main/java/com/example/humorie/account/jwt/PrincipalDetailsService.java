package com.example.humorie.account.jwt;

import com.example.humorie.account.entity.AccountDetail;
import com.example.humorie.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    public final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        AccountDetail accountDetail = accountRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Not found user"));

        PrincipalDetails userDetails = new PrincipalDetails();
        userDetails.setAccountDetail(accountDetail);

        return userDetails;
    }

}
