package com.joeltariku.financetracker.auth.mapper;

import com.joeltariku.financetracker.auth.AuthResult;
import com.joeltariku.financetracker.auth.SignupRequest;
import com.joeltariku.financetracker.auth.dto.AuthResultDto;
import com.joeltariku.financetracker.auth.dto.SignUpRequestDto;

public interface AuthMapper {
    SignupRequest toSignUpRequest(SignUpRequestDto dto);
    AuthResultDto toAuthResultDTO(AuthResult result);
}
