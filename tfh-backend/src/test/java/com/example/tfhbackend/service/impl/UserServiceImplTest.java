package com.example.tfhbackend.service.impl;

import com.example.tfhbackend.dto.UserDTO;
import com.example.tfhbackend.dto.request.ActivateUserRequest;
import com.example.tfhbackend.dto.request.UserRequest;
import com.example.tfhbackend.mapper.Mapper;
import com.example.tfhbackend.model.User;
import com.example.tfhbackend.model.exception.CustomRuntimeException;
import com.example.tfhbackend.model.exception.EntityNotFoundException;
import com.example.tfhbackend.model.fixture.UserFixture;
import com.example.tfhbackend.repository.UserRepository;
import com.example.tfhbackend.validator.UserValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private final String phone = "+37369999999";
    private final int page = 1;
    private final int perPage = 10;
    @Mock
    private UserRepository userRepository;
    @Mock
    private Mapper<User, UserDTO> mapper;
    @Mock
    private UserValidator validator;
    @Mock
    private Authentication authentication;
    @Captor
    private ArgumentCaptor<Pageable> pageCaptor;
    private UserServiceImpl userService;
    private User user;
    private UserDTO userDTO;
    private final UserRequest userRequest = new UserRequest();
    private final ActivateUserRequest activateUserRequest = new ActivateUserRequest(1L);

    @Before
    public void setup() {
        userService = new UserServiceImpl(
                userRepository,
                mapper,
                List.of(validator)
        );

        user = UserFixture.user();
        userDTO = new UserDTO(
                1L,
                "firstName",
                "lastName",
                "+37369999999",
                "user@gmail.com",
                "WAITER",
                null,
                true
        );
        userRequest.setId(1L);
        userRequest.setRole("WAITER");

        when(mapper.map(user))
                .thenReturn(userDTO);
        when(userRepository.findByPhone(phone))
                .thenReturn(Optional.of(user));
        when(userRepository.findAll(pageCaptor.capture()))
                .thenReturn(new PageImpl<>(List.of(user)));
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class)))
                .thenReturn(user);
        when(authentication.getPrincipal())
                .thenReturn(user);
        when(userRepository.findByPhone(user.getPhone()))
                .thenReturn(Optional.of(user));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void loadUserByUsername_whenInvoked_correctResult() {
        var result = userService.loadUserByUsername(phone);

        assertThat(result).isEqualTo(user);
    }

    @Test
    public void loadUserByUsername_whenNotFound_exceptionThrown() {
        when(userRepository.findByPhone(phone))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.loadUserByUsername(phone))
                .isInstanceOf(CustomRuntimeException.class)
                .hasMessage("User with phone " + phone + " not found");
    }

    @Test
    public void get_whenInvoked_correctPage() {
        userService.get(page, perPage);
        var pageable = pageCaptor.getValue();

        assertThat(pageable.getPageNumber()).isEqualTo(page - 1);
        assertThat(pageable.getPageSize()).isEqualTo(perPage);
    }

    @Test
    public void get_whenInvoked_correctResult() {
        var userPage = userService.get(page, perPage);

        assertThat(userPage.getContent())
                .hasSize(1)
                .containsExactly(userDTO);
    }

    @Test
    public void getById_whenNotFound_exceptionThrown() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getById(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User with id 1 not found");
    }

    @Test
    public void getById_whenInvoked_correctResult() {
        var result = userService.getById(1L);

        assertThat(result).isEqualTo(userDTO);
    }

    @Test
    public void add_whenInvoked_validatorsCalled() {
        userService.add(userRequest);

        verify(validator).validate(userRequest);
    }

    @Test
    public void add_whenInvoked_correctResult() {
        var result = userService.add(userRequest);

        assertThat(result).isEqualTo(userDTO);
    }

    @Test
    public void update_whenInvoked_correctResult() {
        userRequest.setFirstName("firstName_updated");
        userRequest.setLastName("lastName_updated");

        userService.update(userRequest);

        assertThat(user.getFirstName()).isEqualTo("firstName_updated");
        assertThat(user.getLastName()).isEqualTo("lastName_updated");
    }

    @Test
    public void remove_whenInvoked_callsRepo() {
        userService.remove(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    public void activateUser_whenInvoked_setConfirmFlagToTrue() {
        user.setConfirmed(false);

        userService.activateUser(activateUserRequest);

        assertThat(user.getConfirmed()).isTrue();
    }

    @Test
    public void activateUser_whenInvoked_messageReturned() {
        var result = userService.activateUser(activateUserRequest);

        assertThat(result.getMessage()).isEqualTo("User successfully activated");
    }

    @Test
    public void getCurrentUser_whenInvokedPhoneNotFound_exceptionThrown() {
        when(userRepository.findByPhone(user.getPhone()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getCurrentLoggedUser())
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Error loading current logged user");
    }

    @Test
    public void getCurrentUser_whenInvoked_correctResult() {
        var result = userService.getCurrentLoggedUser();

        assertThat(result).isEqualTo(userDTO);
    }
}