package com.github.r1cardopereira.repository;

import com.github.r1cardopereira.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}