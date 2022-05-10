package com.example.tfhbackend.service.impl;

import com.example.tfhbackend.config.JwtTokenUtil;
import com.example.tfhbackend.dto.JwtDTO;
import com.example.tfhbackend.dto.MessageDTO;
import com.example.tfhbackend.dto.request.AuthenticationRequest;
import com.example.tfhbackend.dto.request.UserRequest;
import com.example.tfhbackend.model.User;
import com.example.tfhbackend.model.exception.AuthenticationException;
import com.example.tfhbackend.service.AuthenticationService;
import com.example.tfhbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    @Transactional
    public MessageDTO register(UserRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        userService.add(request);
        return new MessageDTO("User successfully registered. You will be notified via email once the user is activated.");
    }

    @Override
    @Transactional
    public JwtDTO authenticate(AuthenticationRequest request) {
        String phone = request.getPhone();
        UserDetails userDetails = getUserDetails(phone);
        User user = (User) userDetails;
        if (!user.getConfirmed())
            throw new AuthenticationException("You can't login because you are not activated yet");
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            user.setDeviceId(request.getDeviceId());
            return generateAuthenticationToken(request, userDetails);
        }
        throw new AuthenticationException("Authentication error");
    }

    private UserDetails getUserDetails(String phone) {
        try {
            return userService.loadUserByUsername(phone);
        } catch (UsernameNotFoundException e) {
            throw new AuthenticationException("Authentication error");
        }
    }

    private JwtDTO generateAuthenticationToken(AuthenticationRequest request, UserDetails userDetails) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getPhone(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenUtil.generateToken(userDetails, ((User) userDetails).getRole().name(), request.isRemember());
        String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

        return new JwtDTO(accessToken, refreshToken);
    }
}
