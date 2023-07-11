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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

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
    void save() {
        UserRequest userRequest = new UserRequest("Erick Gabriel", "abc@gmail.com", "123");
        User user = User.builder().build();

        when(mapper.toEntity(any(UserRequest.class))).thenReturn(user);
        when(repository.save(any(User.class))).thenReturn(Mono.just(user));

        Mono<User> result = service.save(userRequest);

        StepVerifier.create(result)
                .expectNextMatches(Objects::nonNull)
                .expectComplete()
                .verify();

        verify(mapper, times(1)).toEntity(any(UserRequest.class));
        verify(repository, times(1)).save(any(User.class));
    }
}