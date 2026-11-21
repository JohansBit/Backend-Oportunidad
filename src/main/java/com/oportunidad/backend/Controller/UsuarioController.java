package com.oportunidad.backend.Controller;

import com.oportunidad.backend.Model.UsuarioModel;
import com.oportunidad.backend.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody UsuarioModel usuario){
        try {
            UsuarioModel guardado = usuarioService.registrarUsuario(usuario);
                return ResponseEntity.status(HttpStatus.CREATED).body(guardado);

        }catch(IllegalArgumentException e){
                return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales){
        String correo = credenciales.get("correo");
        String clave = credenciales.get("clave");

        return usuarioService.autenticar(correo, clave)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("mensaje", "Correo o clave incorrectos")));
    }

    @GetMapping
    public List<UsuarioModel> listarUsuarios(){
        return usuarioService.obtenerUsuarios();
    }

    @PutMapping("/{id}/rol")
    public ResponseEntity<?> cambiarRol(
            @PathVariable Long id,
            @RequestParam String nuevoRol,
            @RequestParam Long adminId){
        try{
            UsuarioModel actualizado = usuarioService.actualizarRol(id,nuevoRol, adminId);
            return ResponseEntity.status(HttpStatus.CREATED).body(actualizado);
        }catch(SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("mensaje", e.getMessage()));
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        }
    }
}
