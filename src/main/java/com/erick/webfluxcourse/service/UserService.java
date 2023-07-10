package com.erick.webfluxcourse.service;

import com.erick.webfluxcourse.entity.User;
import com.erick.webfluxcourse.mapper.UserMapper;
import com.erick.webfluxcourse.model.request.UserRequest;
import com.erick.webfluxcourse.repository.UserRepository;
import com.erick.webfluxcourse.service.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Mono<User> save(final UserRequest userRequest) {
        return userRepository.save(userMapper.toEntity(userRequest));
    }

    public Mono<User> findById(final String id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new ObjectNotFoundException(
                                format("Object not found! Id: %s, Type: %s", id, User.class.getSimpleName())
                        )
                ));
    }

    public Flux<User> findAll() {
        return userRepository.findAll()
                .switchIfEmpty(Mono.error(
                        new ObjectNotFoundException("No users found!")
                ));
    }
}
