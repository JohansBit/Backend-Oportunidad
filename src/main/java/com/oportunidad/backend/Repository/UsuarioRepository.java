package com.oportunidad.backend.Repository;

import com.oportunidad.backend.Model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {
    Optional<UsuarioModel> findByCorreoAndClave(String correo, String clave);

    boolean existByCorreo(String correo);
}