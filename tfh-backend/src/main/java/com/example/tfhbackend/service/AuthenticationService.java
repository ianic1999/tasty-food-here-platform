package com.example.tfhbackend.service;

import com.example.tfhbackend.dto.MessageDTO;
import com.example.tfhbackend.dto.request.UserRequest;
import com.example.tfhbackend.model.exception.UserException;

public interface AuthenticationService {
    MessageDTO register(UserRequest request) throws UserException;
}
