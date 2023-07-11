package com.erick.webfluxcourse.service;

import com.erick.webfluxcourse.entity.User;
import com.erick.webfluxcourse.mapper.UserMapper;
import com.erick.webfluxcourse.model.request.UserRequest;
import com.erick.webfluxcourse.repository.UserRepository;
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

}