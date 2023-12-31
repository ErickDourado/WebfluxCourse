package com.erick.webfluxcourse.service;

import com.erick.webfluxcourse.entity.User;
import com.erick.webfluxcourse.mapper.UserMapper;
import com.erick.webfluxcourse.model.request.UserRequest;
import com.erick.webfluxcourse.repository.UserRepository;
import com.erick.webfluxcourse.service.exception.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;
    @Mock
    private UserMapper mapper;
    @InjectMocks
    private UserService service;

    @Test
    void testSave() {
        UserRequest userRequest = new UserRequest("Erick Gabriel", "abc@gmail.com", "123");
        User userEntity = User.builder().build();

        when(mapper.toEntity(any(UserRequest.class))).thenReturn(userEntity);
        when(repository.save(any(User.class))).thenReturn(Mono.just(userEntity));

        Mono<User> result = service.save(userRequest);

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass().equals(User.class))
                .expectComplete()
                .verify();

        verify(mapper, times(1)).toEntity(any(UserRequest.class));
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void testFindById() {
        when(repository.findById(anyString())).thenReturn(Mono.just(User.builder().build()));

        Mono<User> result = service.findById("123");

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass().equals(User.class))
                .expectComplete()
                .verify();

        verify(repository, times(1)).findById(anyString());
    }

    @Test
    void testFindAll() {
        when(repository.findAll()).thenReturn(Flux.just(User.builder().build()));

        Flux<User> result = service.findAll();

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass().equals(User.class))
                .expectComplete()
                .verify();

        verify(repository, times(1)).findAll();
    }

    @Test
    void testUpdate(){
        UserRequest userRequest = new UserRequest("Erick Gabriel", "abc@gmail.com", "123");
        User userEntity = User.builder().build();

        when(repository.findById(anyString())).thenReturn(Mono.just(userEntity));
        when(mapper.toEntity(any(UserRequest.class), any(User.class))).thenReturn(userEntity);
        when(repository.save(any(User.class))).thenReturn(Mono.just(userEntity));

        Mono<User> result = service.update("123", userRequest);

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass().equals(User.class))
                .expectComplete()
                .verify();

        verify(repository, times(1)).findById(anyString());
        verify(mapper, times(1)).toEntity(any(UserRequest.class), any(User.class));
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void testDelete(){
        User userEntity = User.builder().build();

        when(repository.findAndRemove(anyString())).thenReturn(Mono.just(userEntity));

        Mono<User> result = service.delete("123");

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass().equals(User.class))
                .expectComplete()
                .verify();

        verify(repository, times(1)).findAndRemove(anyString());
    }

    @Test
    void testHandleNotFound() {
        when(repository.findById(anyString())).thenReturn(Mono.empty());

        Mono<User> result = service.findById("123");

        StepVerifier.create(result)
                .expectError(ObjectNotFoundException.class)
                .verify();

        verify(repository, times(1)).findById(anyString());
    }

}