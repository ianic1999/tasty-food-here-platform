package com.example.tfhbackend.service.impl;

import com.example.tfhbackend.dto.MessageDTO;
import com.example.tfhbackend.dto.UserDTO;
import com.example.tfhbackend.dto.request.UserRequest;
import com.example.tfhbackend.mapper.Mapper;
import com.example.tfhbackend.model.User;
import com.example.tfhbackend.model.enums.UserRole;
import com.example.tfhbackend.model.exception.CustomRuntimeException;
import com.example.tfhbackend.model.exception.EntityNotFoundException;
import com.example.tfhbackend.repository.UserRepository;
import com.example.tfhbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Mapper<User, UserDTO> mapper;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByPhone(s)
                .orElseThrow(() -> new UsernameNotFoundException("User with phone " + s + " not found"));
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
        if (phoneAlreadyExist(request.getPhone()))
            throw new CustomRuntimeException("User with phone " + request.getPhone() + " already exist");
        if (emailAlreadyExist(request.getEmail()))
            throw new CustomRuntimeException("User with email " + request.getEmail() + " already exist");

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .confirmed(false)
                .role(UserRole.valueOf(request.getRole()))
                .password(request.getPassword())
                .tables(new ArrayList<>())
                .build();

        return mapper.map(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDTO update(UserRequest request) {
        User user = findUserById(request.getId());
        if (!user.getPhone().equals(request.getPhone()) && phoneAlreadyExist(request.getPhone()))
            throw new CustomRuntimeException("User with phone " + request.getPhone() + " already exist");
        if (!user.getEmail().equals(request.getEmail()) && emailAlreadyExist(request.getEmail()))
            throw new CustomRuntimeException("User with email " + request.getEmail() + " already exist");

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        return mapper.map(user);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO getCurrentLoggedUser() {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return mapper.map(
                userRepository.findByPhone(user.getUsername())
                        .orElseThrow(() -> new EntityNotFoundException("Error loading current logged user"))
        );

    }

    @Override
    @Transactional
    public MessageDTO activateUser(Long id) {
        User user = findUserById(id);
        user.setConfirmed(true);
        return new MessageDTO("User successfully activated");
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    private boolean phoneAlreadyExist(String phone) {
        return userRepository.findByPhone(phone).isPresent();
    }

    private boolean emailAlreadyExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
