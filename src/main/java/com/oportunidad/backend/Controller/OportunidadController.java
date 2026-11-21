package com.oportunidad.backend.Controller;

import com.oportunidad.backend.Model.OportunidadModel;
import com.oportunidad.backend.Service.OportunidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/oportunidades")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class OportunidadController {

    private final OportunidadService oportunidadService;

    @GetMapping
    public List<OportunidadModel> listarTodas() {
        return oportunidadService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OportunidadModel> obtenerPorId(@PathVariable Long id) {
        return oportunidadService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OportunidadModel> crear(@RequestBody OportunidadModel oportunidad) {
        return ResponseEntity.status(HttpStatus.CREATED).body(oportunidadService.crear(oportunidad));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @RequestBody OportunidadModel oportunidad,
            @RequestParam Long solicitanteId) {
        try {
            return ResponseEntity.ok(oportunidadService.actualizar(id, oportunidad, solicitanteId));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("mensaje", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id, @RequestParam Long solicitanteId) {
        try {
            oportunidadService.eliminar(id, solicitanteId);
            return ResponseEntity.noContent().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("mensaje", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        }
    }
}