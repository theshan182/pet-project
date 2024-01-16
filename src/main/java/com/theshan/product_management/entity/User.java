package com.theshan.product_management.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(name = "first_name", length = 30, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 40, nullable = false)
    private String lastName;

    @Column(length = 50, nullable = false)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled;

    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "role_name", length = 10, nullable = false)
    private String roleName;

    @Builder(builderMethodName = "userBuilder")
    public User(Integer id,
            String firstName,
            String lastName,
            String email,
            String password,
            Boolean isEnabled,
            LocalDateTime registeredAt,
            LocalDateTime updatedAt,
            String roleName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.isEnabled = isEnabled;
        this.registeredAt = registeredAt;
        this.updatedAt = updatedAt;
        this.roleName = roleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(isEnabled, user.isEnabled) && Objects.equals(registeredAt, user.registeredAt) && Objects.equals(updatedAt, user.updatedAt) && Objects.equals(roleName, user.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, password, isEnabled, registeredAt, updatedAt, roleName);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isEnabled=" + isEnabled +
                ", registeredAt=" + registeredAt +
                ", updatedAt=" + updatedAt +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
