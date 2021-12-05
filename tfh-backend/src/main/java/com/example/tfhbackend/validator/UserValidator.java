package com.example.tfhbackend.validator;

import com.example.tfhbackend.dto.request.UserRequest;

public interface UserValidator {
    void validate(UserRequest request);
}
