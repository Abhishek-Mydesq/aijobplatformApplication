package com.aijobplatform.user.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_user_email", columnList = "email"),
                @Index(name = "idx_user_active", columnList = "isActive")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false, unique = true, length = 150)
    private String email;
    @Column(nullable = false, length = 255)
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(nullable = false)
    private Boolean isActive = true;
    @Column(nullable = false)
    private Boolean emailVerified = false;
    @Column(length = 500)
    private String refreshToken;
}