package com.github.r1cardopereira.service;


import com.github.r1cardopereira.entity.Usuario;
import com.github.r1cardopereira.exception.EntityNotFoundException;
import com.github.r1cardopereira.exception.PasswordInvalidException;
import com.github.r1cardopereira.exception.UsernameUniqueViolationException;
import com.github.r1cardopereira.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario salvar(Usuario usuario) {
        try {
            return usuarioRepository.save(usuario);
        }catch (DataIntegrityViolationException ex){
            throw  new UsernameUniqueViolationException(String.format("Username '%s' já cadastrado", usuario.getUsername()));
        }
    }

    @Transactional(readOnly = true)
    public Usuario getById (Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(String.format("Usuário id=%s não encontrado.", id))
                );
    }

    @Transactional
    public Usuario changePassword(Long id, String senhaAtual, String novaSenha, String confirmaNovaSenha){
        if (!novaSenha.equals(confirmaNovaSenha)){
            throw new PasswordInvalidException("Nova senha não confere com a confirmação de senha");
        }
        Usuario user = getById(id);
        if (!user.getPassword().equals(senhaAtual)){
            throw new PasswordInvalidException("Senha atual não confere com a senha informada");

        }
        user.setPassword(novaSenha);

        return  user;
    }

    @Transactional(readOnly = true)
    public List<Usuario> readAll() {
        return usuarioRepository.findAll();
    }



    @Transactional
    public void deleteById(Long id) {
        Usuario user = getById(id);
        usuarioRepository.deleteById(user.getId());
    }
}
