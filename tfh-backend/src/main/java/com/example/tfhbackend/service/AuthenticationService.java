package com.example.tfhbackend.service;

import com.example.tfhbackend.dto.JwtDTO;
import com.example.tfhbackend.dto.MessageDTO;
import com.example.tfhbackend.dto.request.AuthenticationRequest;
import com.example.tfhbackend.dto.request.UserRequest;
import com.example.tfhbackend.model.exception.AuthenticationException;
import com.example.tfhbackend.model.exception.UserException;

public interface AuthenticationService {
    MessageDTO register(UserRequest request) throws UserException;
    JwtDTO authenticate(AuthenticationRequest request) throws AuthenticationException;
}
