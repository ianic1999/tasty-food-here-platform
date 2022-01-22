package com.example.tfhbackend.service.impl;

import com.example.tfhbackend.dto.UserDTO;
import com.example.tfhbackend.dto.request.UserRequest;
import com.example.tfhbackend.mapper.Mapper;
import com.example.tfhbackend.model.User;
import com.example.tfhbackend.model.enums.UserRole;
import com.example.tfhbackend.model.exception.CustomRuntimeException;
import com.example.tfhbackend.model.fixture.UserFixture;
import com.example.tfhbackend.repository.UserRepository;
import com.example.tfhbackend.validator.UserValidator;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private UserServiceImpl userService;

    private UserRepository userRepository;
    private Mapper<User, UserDTO> mapper;
    private List<UserValidator> validators;

    @Test
    public void add_whenValidationNotPassed_exceptionThrown() {
        UserRequest userRequest = new UserRequest();
        validators = List.of(getUserValidatorStub());
        userService = new UserServiceImpl(
                userRepository,
                mapper,
                validators
        );

        assertThatThrownBy(() -> userService.add(userRequest))
                .isInstanceOf(CustomRuntimeException.class)
                .hasMessage("validation.error");
    }

    @Test
    public void add_whenInvoked_eachValidatorCalledOnce() {
        UserRequest userRequest = new UserRequest();
        UserValidatorSpy validator1 = new UserValidatorSpy();
        UserValidatorSpy validator2 = new UserValidatorSpy();
        UserValidatorSpy validator3 = new UserValidatorSpy();

        userService = new UserServiceImpl(
                userRepository,
                mapper,
                List.of(validator1, validator2, validator3, getUserValidatorStub())
        );

        assertThatThrownBy(() -> userService.add(userRequest))
                .isInstanceOf(CustomRuntimeException.class)
                .hasMessage("validation.error");
        assertThat(validator1.getNrOfInvocations()).isEqualTo(1);
        assertThat(validator2.getNrOfInvocations()).isEqualTo(1);
        assertThat(validator3.getNrOfInvocations()).isEqualTo(1);
    }

    @Test
    public void get_whenInvoked_correctResult() {
        userRepository = mock(UserRepository.class);
        ArgumentCaptor<Pageable> pageCaptor = ArgumentCaptor.forClass(Pageable.class);
        when(userRepository.findAll(pageCaptor.capture()))
                .thenReturn(new PageImpl<>(List.of(UserFixture.user(), UserFixture.user(), UserFixture.user())));

        userService = new UserServiceImpl(
                userRepository,
                new FakeUserMapper(),
                List.of(new DummyUserValidator())
        );

        var users = userService.get(1, 10).getContent();
        assertThat(users).hasSize(3)
                .allSatisfy(user -> {
            assertThat(user.getId()).isEqualTo(1);
            assertThat(user.getFirstName()).isEqualTo("Ion");
            assertThat(user.getLastName()).isEqualTo("Cucu");
            assertThat(user.getPhone()).isEqualTo("+37367898989");
            assertThat(user.getEmail()).isEqualTo("ion@mail.com");
        });

    }

    @Test
    public void save_whenInvoked_setCorrectDataToEntity() {
        UserRequest request = new UserRequest(
                null,
                "Ion",
                "Micu",
                "+37378776655",
                "ion@gmail,com",
                "password",
                "WAITER"
        );

        UserRepository userRepository = new MockUserRepository(request);

        userService = new UserServiceImpl(
                userRepository,
                new DummyUserMapper(),
                List.of(new DummyUserValidator())
        );

        userService.add(request);
    }

    private UserValidator getUserValidatorStub() {
        return request -> {
            throw new CustomRuntimeException("validation.error");
        };
    }

    private static class UserValidatorSpy implements UserValidator {
        private int nrOfInvocations = 0;

        public int getNrOfInvocations() {
            return nrOfInvocations;
        }

        @Override
        public void validate(UserRequest request) {
            this.nrOfInvocations++;
        }
    }

    private static class DummyUserValidator implements UserValidator {
        @Override
        public void validate(UserRequest request) {

        }
    }

    private static class DummyUserMapper implements Mapper<User, UserDTO> {
        @Override
        public UserDTO map(User entity) {
            return null;
        }
    }

    private static class FakeUserMapper implements Mapper<User, UserDTO> {

        @Override
        public UserDTO map(User entity) {
            return new UserDTO(
                    1L,
                    "Ion",
                    "Cucu",
                    "+37367898989",
                    "ion@mail.com",
                    "WAITER",
                    true
            );
        }

    }

    private static class MockUserRepository implements UserRepository {

        private UserRequest expectedUserData;

        public MockUserRepository(UserRequest expectedUserData) {
            this.expectedUserData = expectedUserData;
        }

        @Override
        public <S extends User> Optional<S> findOne(Example<S> example) {
            return Optional.empty();
        }

        @Override
        public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
            return null;
        }

        @Override
        public <S extends User> long count(Example<S> example) {
            return 0;
        }

        @Override
        public <S extends User> boolean exists(Example<S> example) {
            return false;
        }

        @Override
        public Page<User> findAll(Pageable pageable) {
            return null;
        }

        @Override
        public <S extends User> S save(S entity) {
            assertThat(entity.getFirstName()).isEqualTo(expectedUserData.getFirstName());
            assertThat(entity.getLastName()).isEqualTo(expectedUserData.getLastName());
            assertThat(entity.getPhone()).isEqualTo(expectedUserData.getPhone());
            assertThat(entity.getEmail()).isEqualTo(expectedUserData.getEmail());
            assertThat(entity.getRole()).isEqualTo(UserRole.WAITER);
            assertThat(entity.getPassword()).isEqualTo(expectedUserData.getPassword());
            assertThat(entity.getConfirmed()).isFalse();

            return null;
        }

        @Override
        public Optional<User> findById(Long aLong) {
            return Optional.empty();
        }

        @Override
        public boolean existsById(Long aLong) {
            return false;
        }

        @Override
        public long count() {
            return 0;
        }

        @Override
        public void deleteById(Long aLong) {

        }

        @Override
        public void delete(User entity) {

        }

        @Override
        public void deleteAllById(Iterable<? extends Long> longs) {

        }

        @Override
        public void deleteAll(Iterable<? extends User> entities) {

        }

        @Override
        public void deleteAll() {

        }

        @Override
        public Optional<User> findByPhone(String phone) {
            return Optional.empty();
        }

        @Override
        public Optional<User> findByEmail(String email) {
            return Optional.empty();
        }

        @Override
        public long countAllByRole(UserRole role) {
            return 0;
        }

        @Override
        public List<User> findAll() {
            return null;
        }

        @Override
        public List<User> findAll(Sort sort) {
            return null;
        }

        @Override
        public List<User> findAllById(Iterable<Long> longs) {
            return null;
        }

        @Override
        public <S extends User> List<S> saveAll(Iterable<S> entities) {
            return null;
        }

        @Override
        public void flush() {

        }

        @Override
        public <S extends User> S saveAndFlush(S entity) {
            return null;
        }

        @Override
        public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) {
            return null;
        }

        @Override
        public void deleteAllInBatch(Iterable<User> entities) {

        }

        @Override
        public void deleteAllByIdInBatch(Iterable<Long> longs) {

        }

        @Override
        public void deleteAllInBatch() {

        }

        @Override
        public User getOne(Long aLong) {
            return null;
        }

        @Override
        public User getById(Long aLong) {
            return null;
        }

        @Override
        public <S extends User> List<S> findAll(Example<S> example) {
            return null;
        }

        @Override
        public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
            return null;
        }
    }
}
