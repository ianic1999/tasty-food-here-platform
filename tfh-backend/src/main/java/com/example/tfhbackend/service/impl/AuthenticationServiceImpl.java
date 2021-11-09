package com.example.tfhbackend.service.impl;

import com.example.tfhbackend.dto.MessageDTO;
import com.example.tfhbackend.dto.request.UserRequest;
import com.example.tfhbackend.model.exception.UserException;
import com.example.tfhbackend.service.AuthenticationService;
import com.example.tfhbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public MessageDTO register(UserRequest request) throws UserException {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        userService.add(request);
        return new MessageDTO("User successfully registered. You will be notified via email once the user is activated.");
    }
}
