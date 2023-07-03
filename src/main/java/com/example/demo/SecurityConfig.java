package com.example.demo;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration      //환경설정 파일임을 선언하는거
@EnableWebSecurity      //모든 요청 url이 시큐리티 제어 받게끔 하는거
@EnableMethodSecurity(prePostEnabled = true)    //QC와 AC에 있는 PreAuthorize어노테이션 활성화를 위해 작성한 것. 로그아웃된 상황에서 글을 쓰거나 댓글을 달면 로그인창으로 리턴된다.
public class SecurityConfig {

    //로그인 안해도 list 등등 볼 수 있게끔 하는거
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests().requestMatchers(
                        new AntPathRequestMatcher("/**")).permitAll()

                //h2 DB에 csrf허가해주는거.
                //csrf토큰이 시큐리티에 의해 자동생성되는데 이 csrf토큰 값을 검증해줌. csrf없으면 해킹당함
                .and()
                    .csrf().ignoringRequestMatchers(
                        new AntPathRequestMatcher("/h2-console/**"))
                //얘까지 추가해야 db확인이 가능하다. 없애면 어떻게 되는지 직접 확인해보기로.
                .and()
                    .headers().addHeaderWriter(new XFrameOptionsHeaderWriter(
                        XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                .and()
                    .formLogin()
                    .loginPage("/user/login")
                    .defaultSuccessUrl("/")
                .and()
                    .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true);
        return http.build();
    }

    //패스워드 빈 생성
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    
    //스프링 시큐리티의 인증 담당.
    //UserSecurityService와 PasswordEncoder를 사용해서 작동
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
