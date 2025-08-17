package com.github.r1cardopereira.web.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioSenhaDto {


    @NotBlank
    @Size(
            min = 6,
            max = 6,
            message = "A senha deve conter 6 digitos."
    )
    private String senhaAtual;

    @NotBlank
    @Size(
            min = 6,
            max = 6,
            message = "A senha deve conter 6 digitos."
    )
    private String novaSenha;

    @NotBlank
    @Size(
            min = 6,
            max = 6,
            message = "A senha deve conter 6 digitos."
    )
    private String confirmaNovaSenha;
}
