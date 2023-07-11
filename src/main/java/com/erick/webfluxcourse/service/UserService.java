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
        return handleNotFound(userRepository.findById(id), id);
    }

    public Flux<User> findAll() {
        return userRepository.findAll()
                .switchIfEmpty(Mono.error(
                        new ObjectNotFoundException("No users found!")
                ));
    }

    public Mono<User> update(final String id, final UserRequest userRequest) {
        return this.findById(id)
                .map(user -> userMapper.toEntity(userRequest, user))
                .flatMap(userRepository::save);
    }

    public Mono<User> delete(final String id) {
        return handleNotFound(userRepository.findAndRemove(id), id);
    }

    private Mono<User> handleNotFound(Mono<User> user, String id) {
        return user
                .switchIfEmpty(Mono.error(
                        new ObjectNotFoundException(
                                format("Object not found! Id: %s, Type: %s", id, User.class.getSimpleName())
                        )
                ));
    }
}
