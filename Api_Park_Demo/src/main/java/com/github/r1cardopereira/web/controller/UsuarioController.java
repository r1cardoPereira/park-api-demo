package com.github.r1cardopereira.web.controller;

import com.github.r1cardopereira.entity.Usuario;
import com.github.r1cardopereira.service.UsuarioService;

import com.github.r1cardopereira.web.dto.UsuarioCreateDto;
import com.github.r1cardopereira.web.dto.UsuarioResponseDto;
import com.github.r1cardopereira.web.dto.UsuarioSenhaDto;
import com.github.r1cardopereira.web.dto.mapper.UsuarioMapper;
import com.github.r1cardopereira.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/usuarios")

public class UsuarioController {

        private final UsuarioService usuarioService;

        /**
         * Este método manipula as requisições POST para criar um novo usuário.
         *
         * @param createDto O DTO com os dados do usuário a ser criado.
         * @return Uma resposta com o status 201 (Created) e o usuário criado.
         */

        @Operation(summary = "Criar um novo Usuário", description = "Recurso para criar um novo usuário", responses = {

                        @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),

                        @ApiResponse(responseCode = "409", description = "Usuário e-mail já cadastrado no sistema", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),

                        @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada invalidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
        })
        @PostMapping
        public ResponseEntity<UsuarioResponseDto> create(@Valid @RequestBody UsuarioCreateDto createDto) {
                Usuario user = usuarioService.salvar(UsuarioMapper.toUsuario(createDto));
                return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(user));
        }

        /**
         * Este método manipula as requisições GET para buscar um usuário pelo ID.
         *
         * @param id O ID do usuário a ser buscado.
         * @return Uma resposta com o status 200 (OK) e o usuário encontrado.
         */

        @Operation(summary = "Recuperar Usuario pelo ID", security = @SecurityRequirement(name = "Security"), description = "Requisição exige Bearer Token. Acesso Restrito a ADMIN|CLIENTE", responses = {

                        @ApiResponse(responseCode = "200", description = "Recurso encontrado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),

                        @ApiResponse(responseCode = "403", description = "Usuário sem permissão para acessar este recurso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),

                        @ApiResponse(responseCode = "404", description = "Recurso não encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
        })
        @GetMapping("/{id}")
        public ResponseEntity<UsuarioResponseDto> readUserById(@PathVariable Long id) {
                Usuario user = usuarioService.getById(id);
                return ResponseEntity.status(HttpStatus.OK).body(UsuarioMapper.toDto(user));

        }

        /**
         * Este método manipula as requisições PATCH para atualizar a senha de um
         * usuário pelo ID.
         *
         * @param id              O ID do usuário a ter a senha atualizada.
         * @param usuarioSenhaDto O DTO com a senha atual, a nova senha e a confirmação
         *                        da nova senha.
         * @return Uma resposta com o status 204 (No Content).
         */

        @Operation(summary = "Altera senha do Usuario", security = @SecurityRequirement(name = "Security"), description = "Requisição exige Bearer Token. Acesso Restrito a ADMIN|CLIENTE.", responses = {

                        @ApiResponse(responseCode = "204", description = "Senha Alterada com Sucesso"),

                        @ApiResponse(responseCode = "400", description = "Senha não confere...", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),

                        @ApiResponse(responseCode = "403", description = "Usuário sem permissão para acessar este recurso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),

                        @ApiResponse(responseCode = "404", description = "Usuário não encontrado!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),

                        @ApiResponse(responseCode = "422", description = "Campos invalidos ou mal formatados.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
        })
        @PatchMapping("/{id}")
        public ResponseEntity<Void> updatePassword(@PathVariable Long id,
                        @Valid @RequestBody UsuarioSenhaDto usuarioSenhaDto) {

                Usuario user = usuarioService.changePassword(id, usuarioSenhaDto.getSenhaAtual(),
                                usuarioSenhaDto.getNovaSenha(), usuarioSenhaDto.getConfirmaNovaSenha());
                return ResponseEntity.noContent().build();
        }

        /**
         * Este método manipula as requisições GET para buscar todos os usuários.
         *
         * @return Uma resposta com o status 200 (OK) e a lista de usuários.
         */

        @Operation(summary = "Listar todos os Usuários Cadastrados", security = @SecurityRequirement(name = "Security"), description = "Requisição exige Bearer Token. Acesso Restrito a ADMIN.", responses = {

                        @ApiResponse(responseCode = "200", description = "Usuários listados com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioCreateDto.class))),

                        @ApiResponse(responseCode = "403", description = "Usuário sem permissão para acessar este recurso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
        })
        @GetMapping
        public ResponseEntity<List<UsuarioResponseDto>> readAll() {
                List<Usuario> users = usuarioService.readAll();
                return ResponseEntity.ok(UsuarioMapper.toListDto(users));

        }

        /**
         * Este método manipula as requisições DELETE para remover um usuário pelo ID.
         *
         * @param id O ID do usuário a ser removido.
         * @return Uma resposta com o status 204 (No Content) se removido com sucesso.
         */
        @Operation(summary = "Remover Usuário pelo ID", security = @SecurityRequirement(name = "Security"), description = "Requisição exige Bearer Token. Acesso Restrito a ADMIN.", responses = {
                        @ApiResponse(responseCode = "204", description = "Usuário removido com sucesso"),
                        @ApiResponse(responseCode = "403", description = "Usuário sem permissão para acessar este recurso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                        @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteById(@PathVariable Long id) {
                usuarioService.deleteById(id);
                return ResponseEntity.noContent().build();
        }

}
