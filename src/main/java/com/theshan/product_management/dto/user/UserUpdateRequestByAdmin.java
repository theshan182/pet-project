package com.theshan.product_management.dto.user;

import com.theshan.product_management.entity.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequestByAdmin {

    private String firstName;

    private String lastName;

    private String email;

    private boolean isEnabled;

    private List<Role> roles;

    @Builder(builderMethodName = "userUpdateRequestByAdminBuilder")
    public UserUpdateRequestByAdmin(String firstName, String lastName, String email, boolean isEnabled, List<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isEnabled = isEnabled;
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserUpdateRequestByAdmin that)) return false;
        return isEnabled == that.isEnabled && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, isEnabled);
    }

    @Override
    public String toString() {
        return "UserUpdateRequestByAdmin{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", isEnabled=" + isEnabled +
                '}';
    }
}
