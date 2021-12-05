package com.example.tfhbackend.validator;

import com.example.tfhbackend.dto.request.UserRequest;
import com.example.tfhbackend.model.exception.CustomRuntimeException;
import com.example.tfhbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
class PhoneAlreadyExistValidator implements UserValidator{
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public void validate(UserRequest request) {
        if (userRepository.findByPhone(request.getPhone()).isPresent())
            throw new CustomRuntimeException("User with phone " + request.getPhone() + " already exist");
    }
}
