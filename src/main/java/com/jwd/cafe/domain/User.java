package com.jwd.cafe.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The representation of user
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder(setterPrefix = "with")
public class User extends AbstractEntity {
    private Long id;
    private Role role;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Double balance;
    private Integer loyaltyPoints;
    private String activationCode;
    private Boolean isActive;
    private Boolean isBlocked;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", role=" + role +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", balance=" + balance +
                ", loyaltyPoints=" + loyaltyPoints +
                ", activationCode='" + activationCode + '\'' +
                ", isActive=" + isActive +
                ", isBlocked=" + isBlocked +
                '}';
    }
}
