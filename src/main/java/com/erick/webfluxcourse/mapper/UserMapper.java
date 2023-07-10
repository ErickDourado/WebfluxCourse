package com.erick.webfluxcourse.mapper;

import com.erick.webfluxcourse.entity.User;
import com.erick.webfluxcourse.model.request.UserRequest;
import com.erick.webfluxcourse.model.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = IGNORE,
        nullValueCheckStrategy = ALWAYS //Esse cara basicamente não seta valor se for null.
)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(final UserRequest userRequest);

    @Mapping(target = "id", ignore = true)
    User toEntity(final UserRequest userRequest, @MappingTarget User user);

    UserResponse toUserResponse(final User user);

}
