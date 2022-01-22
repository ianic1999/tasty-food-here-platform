package com.example.tfhbackend.model.fixture;

import com.example.tfhbackend.model.User;
import com.example.tfhbackend.model.enums.UserRole;

public class UserFixture {

    private Long id;

    private static final String firstName = "Ion";

    private static final String lastName = "Ciobanu";

    private static final String email = "ion@gmail.com";

    private static final String phone = "+37369999999";

    private static final String password = "password";

    public static User user() {
        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phone(phone)
                .password(password)
                .role(UserRole.WAITER)
                .confirmed(true)
                .build();
    }

    public static User notConfirmedUser() {
        var user = user();
        user.setConfirmed(false);
        return user;
    }

}
