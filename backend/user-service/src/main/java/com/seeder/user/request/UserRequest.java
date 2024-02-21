package com.seeder.user.request;

import com.seeder.user.utils.Constant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    @NotBlank(message = Constant.NAME_NOT_EMPTY)
    @NotNull(message = Constant.NAME_NOT_NUL)
    private String name;
    @Email(message = Constant.VALID_EMAIL)
    @NotNull(message = Constant.EMAIL_NOT_EMPTY)
    private String email;
    @NotBlank(message = Constant.PASSWORD_NOT_EMPTY)
    @Pattern(regexp = Constant.PASSWORD_REGEX, message = Constant.PASSWORD_MESSAGE)
    private String password;
    @NotBlank(message = Constant.CASHKICK_NOT_EMPTY)
    private String cashKickBalance;
}
