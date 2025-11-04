package com.pos.modal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pos.domain.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    @Email(message = "Email should be valid")
    private String email;

    @ManyToOne
    private Store store;

    @Column(nullable = false)
    private String password;
    private String phone;

    @Column(nullable = false)
    private UserRole role;

    private LocalDate createdAt;
    private LocalDate updatedAt;
    private LocalDate lastLoginAt;
}
