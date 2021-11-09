package com.example.tfhbackend.service;

import com.example.tfhbackend.dto.MessageDTO;
import com.example.tfhbackend.dto.UserDTO;
import com.example.tfhbackend.dto.request.UserRequest;
import com.example.tfhbackend.model.exception.UserException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    Page<UserDTO> get(int page, int perPage);
    UserDTO getById(Long id) throws UserException;
    UserDTO add(UserRequest request) throws UserException;
    UserDTO update(UserRequest request) throws UserException;
    void remove(Long id);
    UserDTO getCurrentLoggedUser() throws UserException;
    MessageDTO activateUser(Long id) throws UserException;
}
