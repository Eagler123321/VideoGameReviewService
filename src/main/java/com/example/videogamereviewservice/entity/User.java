package com.example.videogamereviewservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    @Column(nullable = false)
    private String password;
    @Email
    @Column(nullable = false, unique = true)
    private String email;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Column(name = "registered_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime registeredAt;
    @Column(nullable = false, length = 50)
    private String role;
    @Column(length = 500)
    private String description;
}
