package com.example.tfhbackend.controller;

import com.example.tfhbackend.controller.advice.ConstraintViolationAdvice;
import com.example.tfhbackend.controller.advice.CustomExceptionAdvice;
import com.example.tfhbackend.dto.JwtDTO;
import com.example.tfhbackend.dto.MessageDTO;
import com.example.tfhbackend.dto.request.AuthenticationRequest;
import com.example.tfhbackend.dto.request.UserRequest;
import com.example.tfhbackend.model.User;
import com.example.tfhbackend.model.exception.CustomRuntimeException;
import com.example.tfhbackend.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.TransactionSystemException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private AuthenticationService authenticationService;

    private MockMvc mockMvc;

    private UserRequest userRequest;
    private MessageDTO message = new MessageDTO("Success");
    private AuthenticationRequest authenticationRequest;
    private JwtDTO jwt;

    @Before
    public void setup() {
        AuthenticationController controller = new AuthenticationController(authenticationService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new CustomExceptionAdvice(), new ConstraintViolationAdvice())
                .build();

        userRequest = new UserRequest(
                null,
                "Micu",
                "Nicu",
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
        jwt = new JwtDTO(
                "access_token",
                "refresh_token"
        );

        when(authenticationService.register(userRequest)).thenReturn(message);
        when(authenticationService.authenticate(authenticationRequest)).thenReturn(jwt);
    }

    @Test
    public void register_whenInvoked_correctResponse() throws Exception {
        mockMvc.perform(
                        post("/api/auth/register")
                                .content(objectMapper.writeValueAsString(userRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message", is(message.getMessage())));

    }

    @Test
    public void register_whenExceptionThrown_badRequest() throws Exception {
        CustomRuntimeException exception = new CustomRuntimeException("register.error");
        doThrow(exception).when(authenticationService).register(userRequest);

        mockMvc.perform(
                        post("/api/auth/register")
                                .content(objectMapper.writeValueAsString(userRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.key", is("validations")))
                .andExpect(jsonPath("$.messages.no_field[0]", is("register.error")));
    }

    @Test
    public void register_whenConstraintViolationException_badRequestAndCorrectResponse() throws Exception {
        ConstraintViolation<User> v1 = mock(ConstraintViolation.class);
        ConstraintViolation<User> v2 = mock(ConstraintViolation.class);
        Path path1 = mock(Path.class);
        Path path2 = mock(Path.class);
        when(path1.toString()).thenReturn("phone");
        when(path2.toString()).thenReturn("email");
        when(v1.getPropertyPath()).thenReturn(path1);
        when(v2.getPropertyPath()).thenReturn(path2);
        when(v1.getMessage()).thenReturn("INVALID_PHONE");
        when(v2.getMessage()).thenReturn("INVALID_EMAIL");
        ConstraintViolationException exception = new ConstraintViolationException(Set.of(v1, v2));
        TransactionSystemException transactionSystemException = mock(TransactionSystemException.class);
        when(transactionSystemException.getRootCause()).thenReturn(exception);

        doThrow(transactionSystemException)
                .when(authenticationService)
                .register(userRequest);

        mockMvc.perform(
                        post("/api/auth/register")
                                .content(objectMapper.writeValueAsString(userRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.key", is("validations")))
                .andExpect(jsonPath("$.messages.phone[0]", is("INVALID_PHONE")))
                .andExpect(jsonPath("$.messages.email[0]", is("INVALID_EMAIL")));
    }

    @Test
    public void login_whenInvoked_correctResponse() throws Exception {
        mockMvc.perform(
                        post("/api/auth/login")
                                .content(objectMapper.writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken", is(jwt.getAccessToken())))
                .andExpect(jsonPath("$.data.refreshToken", is(jwt.getRefreshToken())));

    }

    @Test
    public void login_whenExceptionThrown_badRequest() throws Exception {
        CustomRuntimeException exception = new CustomRuntimeException("auth.error");
        doThrow(exception).when(authenticationService).authenticate(authenticationRequest);

        mockMvc.perform(
                        post("/api/auth/login")
                                .content(objectMapper.writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.key", is("validations")))
                .andExpect(jsonPath("$.messages.no_field[0]", is("auth.error")));
    }

}