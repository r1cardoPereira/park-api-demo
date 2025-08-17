package com.github.r1cardopereira;

import com.github.r1cardopereira.web.dto.UsuarioCreateDto;
import com.github.r1cardopereira.web.dto.UsuarioResponseDto;
import com.github.r1cardopereira.web.dto.UsuarioSenhaDto;
import com.github.r1cardopereira.web.exception.ErrorMessage;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/usuarios/usuarios-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/usuarios/usuarios-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

public class UsuarioIT {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void createUsuario_ComUsernameEPasswordValidos_RetornarUsuarioCriadoComStatus201() {

        UsuarioResponseDto responseBody = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("andre@dotmail.com", "654321"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UsuarioResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getId()).isNotNull();
        Assertions.assertThat(responseBody.getUsername()).isEqualTo("andre@dotmail.com");
        Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENTE");

    }

    @Test
    public void createUsuario_ComUsernameInvalido_RetornarErrorMessageComStatus422() {

        ErrorMessage responseBody = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("andre@dotmail", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
        Assertions.assertThat(responseBody.getMethod()).isEqualTo("POST");

        webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("andre@", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
        Assertions.assertThat(responseBody.getMethod()).isEqualTo("POST");

        webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("andre@dotmail.", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
        Assertions.assertThat(responseBody.getMethod()).isEqualTo("POST");

        webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
        Assertions.assertThat(responseBody.getMethod()).isEqualTo("POST");

    }

    @Test
    public void createUsuario_ComPasswordInvalido_RetornarErrorMessageComStatus422() {

        ErrorMessage responseBody = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("andre@dotmail.com", "12345"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
        Assertions.assertThat(responseBody.getMethod()).isEqualTo("POST");

        webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("andre@dotmail.com", "1234567"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
        Assertions.assertThat(responseBody.getMethod()).isEqualTo("POST");

        webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("andre@dotmail.com", "0"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
        Assertions.assertThat(responseBody.getMethod()).isEqualTo("POST");

    }

    @Test
    public void createUsuario_ComUsernameDuplicado_RetornarErrorMessageComStatus409() {

        ErrorMessage responseBody = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("ana@email.com", "000000"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);

    }

    @Test
    public void buscarUsuarioPorid_ComIdValido_RetornarUsuarioResponseDtoComStatus200() {

        UsuarioResponseDto responseBudy = webTestClient
                .get()
                .uri("/api/v1/usuarios/100")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UsuarioResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBudy).isNotNull();
        Assertions.assertThat(responseBudy.getUsername()).isEqualTo("ana@email.com");
        Assertions.assertThat(responseBudy.getId()).isEqualTo(100);
        Assertions.assertThat(responseBudy.getRole()).isEqualTo("ADMIN");

    }

    @Test
    public void buscarUsuarioPorid_ComIdInvalido_RetornarErrorMessageComStatus404() {

        ErrorMessage responseBudy = webTestClient
                .get()
                .uri("/api/v1/usuarios/200")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBudy).isNotNull();
        Assertions.assertThat(responseBudy.getStatus()).isEqualTo(404);

    }

    @Test
    public void atualizarSenhaPorId_ComPasswordsValidos_RetornarUsuarioResponseDtoComStatus204() {

        webTestClient
                .patch()
                .uri("/api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("123456", "000000", "000000"))
                .exchange()
                .expectStatus().isNoContent();

    }

    @Test
    public void atualizarSenhaPorId_ComSenhaAtualInvalida_RetornarErrorMessageComStatus400() {

        ErrorMessage responseBody = webTestClient
                .patch()
                .uri("/api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("111111", "000000", "000000"))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

    }

    @Test
    public void atualizarSenhaPorId_ComNovaSenhaEConfirmaNovaSenhaDiferentes_RetornarErrorMessageComStatus400() {

        ErrorMessage responseBody = webTestClient
                .patch()
                .uri("/api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("123456", "111111", "222222"))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

    }

    @Test
    public void atualizarSenhaPorId_ComIdInexistente_RetornarErrorMessageComStatus404() {

        ErrorMessage responseBody = webTestClient
                .patch()
                .uri("/api/v1/usuarios/200")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("123456", "000000", "111111"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);

    }

    @Test
    public void atualizarSenhaPorId_ComNovaSenhaInvalida_RetornarErrorMessageComStatus422() {

        ErrorMessage responseBody = webTestClient
                .patch()
                .uri("/api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("123456", "00000", "000000"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        webTestClient
                .patch()
                .uri("/api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("123456", "1234567", "000000"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        webTestClient
                .patch()
                .uri("/api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("123456", "12345", "000000"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    }

    @Test
    public void listarUsuarios_RetornarUsuarioResponseDtoComStatus200() {

        List<UsuarioResponseDto> responseBody = webTestClient
                .get()
                .uri("/api/v1/usuarios")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UsuarioResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.size()).isEqualTo(3);

    }


    @Test
    public void deletarUsuario_ComIdExistente_RetornarComStatus204() {

        webTestClient
                .delete()
                .uri("/api/v1/usuarios/100")
                .exchange()
                .expectStatus().isNoContent();
            
    }


    @Test
    public void deletarUsuario_ComIdInexistente_RetornarErrorMessageComStatus404(){

        ErrorMessage reponseBody = webTestClient
                    .delete()
                    .uri("/api/v1/usuarios/200")
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();

        Assertions.assertThat(reponseBody).isNotNull();
        Assertions.assertThat(reponseBody.getStatus()).isEqualTo(404);

    }
                
                

        

}
