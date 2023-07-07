package com.example.demo.user;

import com.example.demo.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    //회원가입
    public SiteUser create(String username, String email, String password) {
        SiteUser user = SiteUser.builder()
                .userName(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
        this.userRepository.save(user);
        return user;
    }

    public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByUserName(username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("사용자 정보를 찾을 수 없습니다.");
        }
    }

}
