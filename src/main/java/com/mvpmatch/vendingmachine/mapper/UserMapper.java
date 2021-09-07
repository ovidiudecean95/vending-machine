package com.mvpmatch.vendingmachine.mapper;

import com.mvpmatch.vendingmachine.dto.CreateUserRequest;
import com.mvpmatch.vendingmachine.dto.UpdateUserRequest;
import com.mvpmatch.vendingmachine.dto.view.UserView;
import com.mvpmatch.vendingmachine.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring", uses = PasswordEncoderMapper.class)
public abstract class UserMapper {

    @Mapping(source = "password", target = "password", qualifiedBy = EncodedMapping.class)
    public abstract User createUserRequestToUser(CreateUserRequest createUserRequest);

    public abstract  UserView userToUserView(User user);

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    public abstract void update(UpdateUserRequest request, @MappingTarget User user);

}
