package com.example.carbook.mapper;

import com.example.carbook.config.MapstructConfig;
import com.example.carbook.model.dto.UserRequest;
import com.example.carbook.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public abstract class UserRequestMapper {
    public abstract User userDtoToUser(UserRequest userRequest);

}
