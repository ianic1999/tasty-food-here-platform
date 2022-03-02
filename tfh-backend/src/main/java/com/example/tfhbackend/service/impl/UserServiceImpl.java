package com.example.tfhbackend.service.impl;

import com.example.tfhbackend.dto.MessageDTO;
import com.example.tfhbackend.dto.UserDTO;
import com.example.tfhbackend.dto.request.ActivateUserRequest;
import com.example.tfhbackend.dto.request.UserRequest;
import com.example.tfhbackend.mapper.Mapper;
import com.example.tfhbackend.model.User;
import com.example.tfhbackend.model.enums.UserRole;
import com.example.tfhbackend.model.exception.CustomRuntimeException;
import com.example.tfhbackend.model.exception.EntityNotFoundException;
import com.example.tfhbackend.repository.UserRepository;
import com.example.tfhbackend.service.UserService;
import com.example.tfhbackend.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Mapper<User, UserDTO> mapper;
    private final List<UserValidator> validators;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String phone) {
        return userRepository.findByPhone(phone)
                .orElseThrow(() -> new CustomRuntimeException("User with phone " + phone + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> get(int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage);
        return userRepository.findAll(pageable).map(mapper::map);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getById(Long id) {
        return mapper.map(findUserById(id));
    }

    @Override
    @Transactional
    public UserDTO add(UserRequest request) {
        validators.forEach(validator -> validator.validate(request));

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .confirmed(false)
                .role(UserRole.valueOf(request.getRole()))
                .password(request.getPassword())
                .tables(Collections.emptyList())
                .build();

        return mapper.map(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDTO update(UserRequest request) {    
        User user = findUserById(request.getId());

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        return mapper.map(user);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getCurrentLoggedUser() {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return mapper.map(
                userRepository.findByPhone(user.getUsername())
                        .orElseThrow(() -> new EntityNotFoundException("Error loading current logged user"))
        );

    }

    @Override
    @Transactional
    public MessageDTO activateUser(ActivateUserRequest request) {
        User user = findUserById(request.getUserId());
        user.setConfirmed(true);
        return new MessageDTO("User successfully activated");
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }
}
