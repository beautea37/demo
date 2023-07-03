package com.example.demo.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    //loadUserByUsername 메서드는 사용자명으로 비밀번호를 조회하여 리턴하는 메서드
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 데이터베이스에서 사용자 이름으로 사용자를 검색
        Optional<SiteUser> _siteUser = this.userRepository.findByUserName(username);

        // 사용자가 데이터베이스에 없으면, UsernameNotFoundException을 발생시키기
        if (_siteUser.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        }

        // 사용자가 있으면, _siteUser에서 사용자 정보 가져오기
        SiteUser siteUser = _siteUser.get();

        // 사용자에게 할당할 권한 목록 생성하기
        List<GrantedAuthority> authorities = new ArrayList<>();

        // username이 "admin"일 경우, 사용자에게 ADMIN 권한 부여
        if ("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        } else {
            // username이 "admin"이 아닌 경우, 사용자에게 USER 권한 부여
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }
        // 생성된 권한 목록을 포함하여 UserDetails 객체를 생성해서 반환
        return new User(siteUser.getUserName(), siteUser.getPassword(), authorities);
    }
}
