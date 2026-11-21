package com.oportunidad.backend.Repository;

import com.oportunidad.backend.Model.OportunidadModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OportunidadRepository extends JpaRepository<OportunidadModel,Long>{
    List<OportunidadModel> findByResponsableId(Long responsableId);
    List<OportunidadModel> findByEstado(String estado);
}
