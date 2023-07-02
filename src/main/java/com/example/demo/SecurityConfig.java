package com.example.demo;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration      //환경설정 파일임을 선언하는거
@EnableWebSecurity      //모든 요청 url이 시큐리티 제어 받게끔 하는거
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
                        XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN
                ));
        return http.build();
    }
}
