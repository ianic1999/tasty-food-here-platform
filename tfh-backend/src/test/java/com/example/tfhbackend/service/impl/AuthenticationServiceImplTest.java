package com.example.tfhbackend.service.impl;

import com.example.tfhbackend.config.JwtTokenUtil;
import com.example.tfhbackend.dto.request.AuthenticationRequest;
import com.example.tfhbackend.dto.request.UserRequest;
import com.example.tfhbackend.model.User;
import com.example.tfhbackend.model.enums.UserRole;
import com.example.tfhbackend.model.exception.AuthenticationException;
import com.example.tfhbackend.model.fixture.UserFixture;
import com.example.tfhbackend.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceImplTest {

    private final String accessToken = "access_token";
    private final String refreshToken = "refresh_token";
    @Mock
    private UserService userService;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenUtil jwtTokenUtil;
    @Mock
    private Authentication authentication;
    private AuthenticationServiceImpl authenticationService;
    private UserRequest userRequest;
    private AuthenticationRequest authenticationRequest;
    private User user;

    @Before
    public void setup() {
        authenticationService = new AuthenticationServiceImpl(
                userService,
                passwordEncoder,
                authenticationManager,
                jwtTokenUtil
        );

        userRequest = new UserRequest(
                null,
                "firstName",
                "lastName",
                "+37369999999",
                "user@mail.com",
                "password",
                "WAITER"
        );
        authenticationRequest = new AuthenticationRequest(
                "+37369999999",
                "password",
                false
        );
        user = UserFixture.user();

        when(passwordEncoder.encode("password"))
                .thenReturn("password");
        when(userService.loadUserByUsername(authenticationRequest.getPhone()))
                .thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtTokenUtil.generateToken(user, UserRole.WAITER.name(), authenticationRequest.isRemember()))
                .thenReturn(accessToken);
        when(jwtTokenUtil.generateRefreshToken(user))
                .thenReturn(refreshToken);
        when(passwordEncoder.matches(authenticationRequest.getPassword(), "password"))
                .thenReturn(true);
    }

    @Test
    public void register_whenInvoked_encryptsPassword() {
        authenticationService.register(userRequest);

        verify(passwordEncoder).encode("password");
    }

    @Test
    public void register_whenInvoked_callService() {
        authenticationService.register(userRequest);

        verify(userService).add(userRequest);
    }

    @Test
    public void register_whenInvoked_messageReturned() {
        var message = authenticationService.register(userRequest);

        assertThat(message.getMessage()).isEqualTo("User successfully registered. You will be notified via email once the user is activated.");
    }

    @Test
    public void authenticate_whenInvoked_loadUserByPhone() {
        authenticationService.authenticate(authenticationRequest);

        verify(userService).loadUserByUsername(authenticationRequest.getPhone());
    }

    @Test
    public void authenticate_whenNotConfirmed_authenticationExceptionThrown() {
        when(userService.loadUserByUsername(authenticationRequest.getPhone()))
                .thenReturn(UserFixture.notConfirmedUser());

        assertThatThrownBy(() -> authenticationService.authenticate(authenticationRequest))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("You can't login because you are not activated yet");
    }

    @Test
    public void authenticate_whenPasswordsDoNotMatch_authenticationExceptionThrown() {
        when(passwordEncoder.matches(authenticationRequest.getPassword(), "password"))
                .thenReturn(false);

        assertThatThrownBy(() -> authenticationService.authenticate(authenticationRequest))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Authentication error");
    }

    @Test
    public void authenticate_whenInvoked_jwtTokenReturned() {
        var jwt = authenticationService.authenticate(authenticationRequest);

        assertThat(jwt.getAccessToken()).isEqualTo(accessToken);
        assertThat(jwt.getRefreshToken()).isEqualTo(refreshToken);
    }

}