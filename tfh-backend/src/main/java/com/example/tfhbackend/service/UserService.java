package com.example.tfhbackend.service;

import com.example.tfhbackend.dto.MessageDTO;
import com.example.tfhbackend.dto.UserDTO;
import com.example.tfhbackend.dto.request.UserRequest;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    Page<UserDTO> get(int page, int perPage);
    UserDTO getById(Long id);
    UserDTO add(UserRequest request);
    UserDTO update(UserRequest request);
    void remove(Long id);
    UserDTO getCurrentLoggedUser();
    MessageDTO activateUser(Long id);
}
