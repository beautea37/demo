package com.example.demo.user;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class SiteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userName;

    private String password;

    @Column(unique = true)
    private String email;

    @Builder
    public SiteUser(String userName, String password, String email) {
        this.userName = userName;
        this.password = password;
        this.email = email;
    }
}