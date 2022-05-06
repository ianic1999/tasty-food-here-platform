package com.example.tfhbackend.controller;

import com.example.tfhbackend.controller.advice.ConstraintViolationAdvice;
import com.example.tfhbackend.controller.advice.CustomExceptionAdvice;
import com.example.tfhbackend.dto.MessageDTO;
import com.example.tfhbackend.dto.UserDTO;
import com.example.tfhbackend.dto.request.ActivateUserRequest;
import com.example.tfhbackend.dto.request.UserRequest;
import com.example.tfhbackend.model.User;
import com.example.tfhbackend.model.exception.CustomRuntimeException;
import com.example.tfhbackend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.TransactionSystemException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final int page = 1;
    private final int perPage = 10;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    private UserDTO userDTO;
    private UserRequest userRequest;
    private ActivateUserRequest activateUserRequest;

    @Before
    public void setup() {
        UserController controller = new UserController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new CustomExceptionAdvice(), new ConstraintViolationAdvice())
                .build();

        userDTO = new UserDTO(
                1L,
                "Ion",
                "Cucu",
                "+37378998899",
                "user@mail.com",
                "WAITER",
                null,
                true
        );
        userRequest = new UserRequest(
                1L,
                "Ion",
                "Cucu",
                "+37378998899",
                "user@mail.com",
                "password",
                "WAITER"
        );
        activateUserRequest = new ActivateUserRequest(1L);

        when(userService.get(page, perPage))
                .thenReturn(new PageImpl<>(List.of(userDTO), PageRequest.of(page - 1, perPage), 1));
        when(userService.getById(1L)).thenReturn(userDTO);
        when(userService.getCurrentLoggedUser()).thenReturn(userDTO);
        when(userService.update(userRequest)).thenReturn(userDTO);
        when(userService.activateUser(activateUserRequest)).thenReturn(new MessageDTO("Success"));
    }

    @Test
    public void get_whenInvoked_correctResponse() throws Exception {
        mockMvc.perform(
                        get("/api/users")
                                .param("page", String.valueOf(page))
                                .param("perPage", String.valueOf(perPage))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(userDTO.getId().intValue())))
                .andExpect(jsonPath("$.data[0].firstName", is(userDTO.getFirstName())))
                .andExpect(jsonPath("$.data[0].lastName", is(userDTO.getLastName())))
                .andExpect(jsonPath("$.data[0].email", is(userDTO.getEmail())))
                .andExpect(jsonPath("$.data[0].phone", is(userDTO.getPhone())))
                .andExpect(jsonPath("$.data[0].role", is(userDTO.getRole())))
                .andExpect(jsonPath("$.pagination.count", is(1)))
                .andExpect(jsonPath("$.pagination.total", is(1)))
                .andExpect(jsonPath("$.pagination.perPage", is(perPage)))
                .andExpect(jsonPath("$.pagination.currentPage", is(page)))
                .andExpect(jsonPath("$.pagination.links.previous", is(false)))
                .andExpect(jsonPath("$.pagination.links.next", is(false)));
    }

    @Test
    public void getById_whenInvoked_correctResponse() throws Exception {
        mockMvc.perform(get("/api/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(userDTO.getId().intValue())))
                .andExpect(jsonPath("$.data.firstName", is(userDTO.getFirstName())))
                .andExpect(jsonPath("$.data.lastName", is(userDTO.getLastName())))
                .andExpect(jsonPath("$.data.email", is(userDTO.getEmail())))
                .andExpect(jsonPath("$.data.phone", is(userDTO.getPhone())))
                .andExpect(jsonPath("$.data.role", is(userDTO.getRole())));
    }

    @Test
    public void getCurrentLoggedUser_whenInvoked_correctResponse() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(userDTO.getId().intValue())))
                .andExpect(jsonPath("$.data.firstName", is(userDTO.getFirstName())))
                .andExpect(jsonPath("$.data.lastName", is(userDTO.getLastName())))
                .andExpect(jsonPath("$.data.email", is(userDTO.getEmail())))
                .andExpect(jsonPath("$.data.phone", is(userDTO.getPhone())))
                .andExpect(jsonPath("$.data.role", is(userDTO.getRole())));
    }

    @Test
    public void update_whenInvoked_correctResult() throws Exception {
        mockMvc.perform(
                        put("/api/users/{id}", 1L)
                                .content(objectMapper.writeValueAsString(userRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(userDTO.getId().intValue())))
                .andExpect(jsonPath("$.data.firstName", is(userDTO.getFirstName())))
                .andExpect(jsonPath("$.data.lastName", is(userDTO.getLastName())))
                .andExpect(jsonPath("$.data.email", is(userDTO.getEmail())))
                .andExpect(jsonPath("$.data.phone", is(userDTO.getPhone())))
                .andExpect(jsonPath("$.data.role", is(userDTO.getRole())));
    }

    @Test
    public void update_whenExceptionThrown_badRequest() throws Exception {
        var exception = new CustomRuntimeException("update.error");
        doThrow(exception)
                .when(userService)
                .update(userRequest);

        mockMvc.perform(
                        put("/api/users/{id}", 1L)
                                .content(objectMapper.writeValueAsString(userRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.key", is("validations")))
                .andExpect(jsonPath("$.messages.no_field[0]", is("update.error")));
    }

    @Test
    public void update_whenConstraintViolations_badRequestAndCorrectResponse() throws Exception {
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
                .when(userService)
                .update(userRequest);

        mockMvc.perform(
                        put("/api/users/{id}", 1L)
                                .content(objectMapper.writeValueAsString(userRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.key", is("validations")))
                .andExpect(jsonPath("$.messages.phone[0]", is("INVALID_PHONE")))
                .andExpect(jsonPath("$.messages.email[0]", is("INVALID_EMAIL")));
    }

    @Test
    public void activate_whenInvoked_correctResult() throws Exception {
        mockMvc.perform(
                        post("/api/users/activate")
                                .content(objectMapper.writeValueAsString(activateUserRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message", is("Success")));
    }

    @Test
    public void remove_whenInvoked_correctResult() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", 1L))
                .andExpect(status().isNoContent());
    }

}