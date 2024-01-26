package com.theshan.product_management.dto.user;

import com.theshan.product_management.dto.RoleResponse;
import com.theshan.product_management.entity.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class UserResponse {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private boolean isEnabled;
    private List<RoleResponse> roles;
    private Date registeredAt;
    private Date updateAt;

    @Builder(builderMethodName = "userResponseBuilder")
    public UserResponse(int id, String firstName, String lastName, String email, boolean isEnabled, List<RoleResponse> roles, Date registeredAt, Date updateAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isEnabled = isEnabled;
        this.roles = roles;
        this.registeredAt = registeredAt;
        this.updateAt = updateAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserResponse that)) return false;
        return id == that.id && isEnabled == that.isEnabled && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(email, that.email) && Objects.equals(registeredAt, that.registeredAt) && Objects.equals(updateAt, that.updateAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, isEnabled, registeredAt, updateAt);
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", isEnabled=" + isEnabled +
                ", registeredAt=" + registeredAt +
                ", updateAt=" + updateAt +
                '}';
    }
}