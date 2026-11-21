package com.oportunidad.backend.Service;
import com.oportunidad.backend.Model.UsuarioModel;
import com.oportunidad.backend.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioModel registrarUsuario(UsuarioModel usuario){
        if(usuarioRepository.existByCorreo(usuario.getCorreo())){
            throw new IllegalArgumentException("El correo ya esta registrado");
        }
        usuario.setRol("EJECUTIVO");
        return usuarioRepository.save(usuario);
    }

    public Optional<UsuarioModel> autenticar(String correo, String clave){
        return usuarioRepository.findByCorreoAndClave(correo, clave);
    }

    public List<UsuarioModel> obtenerUsuarios(){
        return usuarioRepository.findAll();
    }

    public Optional<UsuarioModel> obtenerPorId(Long id){
        return usuarioRepository.findById(id);
    }

    public UsuarioModel actualizarRol(Long usuarioObjetivoId, String nuevoRol, Long adminId){
        UsuarioModel solicitante = usuarioRepository.findById(adminId)
                .orElseThrow(()-> new IllegalArgumentException("Usuario solicitante no encontrado"));
        if(!solicitante.puedeGestionar()){
            throw new SecurityException("Solo un SUPERVISOR puede modificar roles");
        }
        UsuarioModel objetivo = usuarioRepository.findById(usuarioObjetivoId)
                .orElseThrow(()-> new IllegalArgumentException("Usuario objetivo no encontrado"));

        objetivo.setRol(nuevoRol.toUpperCase());
        return usuarioRepository.save(objetivo);
    }
}
