package com.taxiWithBack.domain.member.service;

import com.taxiWithBack.domain.member.entity.User;
import com.taxiWithBack.domain.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passWordEncoder;
    @Override
    public UserDetails loadUserByUsername(String eMail) throws UsernameNotFoundException {
        User user=userRepository.findByeMail(eMail)
                .orElseThrow(() -> new UsernameNotFoundException(eMail+"을(를) 찾을 수 없습니다."));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEMail())
                .password(passWordEncoder.encode(user.getPassWord()))
                .roles("USER")
                .build();

    }
}
