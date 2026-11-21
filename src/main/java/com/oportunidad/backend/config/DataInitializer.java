package com.oportunidad.backend.config;

import com.oportunidad.backend.Model.UsuarioModel;
import com.oportunidad.backend.Repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner cargarDatosIniciales(UsuarioRepository usuarioRepository) {
        return args -> {
            if (usuarioRepository.count() == 0) {
                usuarioRepository.save(UsuarioModel.builder()
                        .nombre("Supervisor Comercial")
                        .correo("supervisor@duoc.cl")
                        .clave("123456")
                        .direccion("Sede Puente Alto")
                        .rol("SUPERVISOR")
                        .build());
            }
        };
    }
}