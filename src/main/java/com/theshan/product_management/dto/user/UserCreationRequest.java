package com.theshan.product_management.dto.user;

import com.theshan.product_management.entity.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class UserCreationRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private boolean isEnabled;

    private List<Role> roles;

    @Builder(builderMethodName = "userCreationRequestBuilder")
    public UserCreationRequest(String firstName, String lastName, String email, String password, boolean isEnabled, List<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.isEnabled = isEnabled;
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserCreationRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isEnabled=" + isEnabled +
                '}';
    }
}
