package com.seeder.user.request;

import com.seeder.user.utils.Constant;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
    @Pattern(regexp = Constant.PASSWORD_REGEX, message = Constant.PASSWORD_MESSAGE)
    private String password;
    private String cashKickBalance;
}
