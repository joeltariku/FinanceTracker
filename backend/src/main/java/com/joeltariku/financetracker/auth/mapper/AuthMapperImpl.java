package com.joeltariku.financetracker.auth.mapper;

import com.joeltariku.financetracker.auth.AuthResult;
import com.joeltariku.financetracker.auth.SignupRequest;
import com.joeltariku.financetracker.auth.dto.AuthResultDto;
import com.joeltariku.financetracker.auth.dto.SignUpRequestDto;
import com.joeltariku.financetracker.user.User;
import com.joeltariku.financetracker.user.dto.UserDto;

public class AuthMapperImpl implements AuthMapper {
    @Override
    public SignupRequest toSignUpRequest(SignUpRequestDto dto) {
        return new SignupRequest(
                dto.firstName(),
                dto.lastName(),
                dto.userName(),
                dto.email(),
                dto.password()
        );
    }

    @Override
    public AuthResultDto toAuthResultDTO(AuthResult result) {
        User user = result.user();
        UserDto userDto = new UserDto(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getUserName(),
            user.getEmail()
        );
        return new AuthResultDto(
                userDto,
                result.token()
        );
    }
}
