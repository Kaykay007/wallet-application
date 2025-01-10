package com.korede.wallet.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistrationRequestDto {
    @NotBlank(message = "username cannot be blank")
    @Size(min = 3,message = "username must be at least 3 characters long and maximum 50", max = 50)

    private String username;
    @NotBlank(message = " password cannot be blank")
    private String password;
    @Email
    private String email;


}
