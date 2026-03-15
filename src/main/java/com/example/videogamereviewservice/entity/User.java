package com.example.videogamereviewservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50)
    private String nickname;
    @Column(length = 50)
    private String username;

    private String password;
    @Email
    private String email;

    private String avatarUrl;

    private LocalDateTime registeredAt;
    @Column(length = 50)
    private String role;
    @Column(length = 500)
    private String description;
}
