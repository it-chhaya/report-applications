package co.istad.reporting.core.user;

import co.istad.reporting.core.auth.dto.RegisterRequest;
import co.istad.reporting.core.auth.dto.RegisterResponse;
import co.istad.reporting.domain.primary.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User fromRegisterRequest(RegisterRequest registerRequest);

    RegisterResponse toRegisterResponse(User user);

}
