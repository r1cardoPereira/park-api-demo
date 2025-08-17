package com.github.r1cardopereira.web.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UsuarioCreateDto {

    @NotBlank
    @Email(
            message = "Formato do email invalido",
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    )
    private String username;

    @NotBlank
    @Size(
            min = 6,
            max = 6,
            message = "A Senha deve conter 6 digitos"
    )
    private String password;
}


