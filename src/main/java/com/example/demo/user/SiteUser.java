package com.example.demo.user;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.PackagePrivate;

@Getter
@Setter
@Entity
//시큐리티에 이미 User클래스가 있기 때문에 User는 피해서 네이밍 해야 함.
public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userName;

    private String password;

    @Column(unique = true)
    private String email;

}
