package com.oportunidad.backend.Service;

import com.oportunidad.backend.Model.OportunidadModel;
import com.oportunidad.backend.Model.UsuarioModel;
import com.oportunidad.backend.Repository.OportunidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OportunidadService {

    private final OportunidadRepository oportunidadRepository;
    private final UsuarioService usuarioService;

    public List<OportunidadModel> listarTodas() {
        return oportunidadRepository.findAll();
    }

    public Optional<OportunidadModel> obtenerPorId(Long id) {
        return oportunidadRepository.findById(id);
    }

    public OportunidadModel crear(OportunidadModel oportunidad) {
        if (oportunidad.getEstado() == null || oportunidad.getEstado().isBlank()) {
            oportunidad.setEstado("Evento identificado");
        }


        if (oportunidad.getResponsableId() == null && oportunidad.getCreadoporId() != null) {
            usuarioService.obtenerPorId(oportunidad.getCreadoporId()).ifPresent(creador -> {
                oportunidad.setResponsableId(creador.getId());
                oportunidad.setResponsableNombre(creador.getNombre());
            });
        } else if (oportunidad.getResponsableId() != null) {
            usuarioService.obtenerPorId(oportunidad.getResponsableId()).ifPresent(resp ->
                    oportunidad.setResponsableNombre(resp.getNombre())
            );
        }

        return oportunidadRepository.save(oportunidad);
    }

    public OportunidadModel actualizar(Long id, OportunidadModel datos, Long solicitanteId) {
        OportunidadModel existente = oportunidadRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Oportunidad no encontrada"));

        existente.setNombre(datos.getNombre());
        existente.setTipo(datos.getTipo());
        existente.setObservaciones(datos.getObservaciones());
        existente.setFuente(datos.getFuente());
        existente.setPrioridad(datos.getPrioridad());
        existente.setPotencial(datos.getPotencial());
        existente.setEstado(datos.getEstado());
        existente.setObservaciones(datos.getObservaciones());


        if (datos.getResponsableId() != null && !datos.getResponsableId().equals(existente.getResponsableId())) {
            UsuarioModel solicitante = usuarioService.obtenerPorId(solicitanteId)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario solicitante no encontrado"));

            if (!solicitante.esAdmin()) {
                throw new SecurityException("Solo un SUPERVISOR puede asignar o cambiar responsables");
            }

            UsuarioModel nuevoResponsable = usuarioService.obtenerPorId(datos.getResponsableId())
                    .orElseThrow(() -> new IllegalArgumentException("Responsable asignado no encontrado"));

            existente.setResponsableId(nuevoResponsable.getId());
            existente.setResponsableNombre(nuevoResponsable.getNombre());
        }

        return oportunidadRepository.save(existente);
    }

    public void eliminar(Long id, Long solicitanteId) {
        UsuarioModel solicitante = usuarioService.obtenerPorId(solicitanteId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario solicitante no encontrado"));

        if (!solicitante.esAdmin()) {
            throw new SecurityException("Solo un SUPERVISOR puede eliminar oportunidades");
        }

        if (!oportunidadRepository.existsById(id)) {
            throw new IllegalArgumentException("Oportunidad no encontrada");
        }

        oportunidadRepository.deleteById(id);
    }
}
