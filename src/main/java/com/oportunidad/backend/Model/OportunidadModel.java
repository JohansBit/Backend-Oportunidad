package com.oportunidad.backend.Model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name= "oportunidades")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OportunidadModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable =false)
    private String tipo;

    @Column(nullable = false)
    private String fuente;

    @Column(nullable = false)
    private String prioridad;

    @Column(nullable = false)
    private String potencial;

    @Column(nullable = false)
    @Builder.Default
    private String estado = "Evento Identificado";

    @Column(length = 1000)
    private String observaciones;

    private Long creadoporId;
    private Long responsableId;
    private String responsableNombre;

    private LocalDateTime fechaDeteccion;
    private LocalDateTime fechaActualizacion;

    @PrePersist
    protected void onCreate(){
        this.fechaDeteccion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate(){
        this.fechaActualizacion = LocalDateTime.now();
    }
}
