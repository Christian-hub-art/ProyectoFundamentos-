package com.uniride.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Viaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "conductor_id")
    private Usuario conductor;
    
    private String origen;
    private String destino;
    private LocalDateTime fechaHora;
    private Integer asientosDisponibles;
    private Double precio;
    private String descripcion;
    private String estado; // DISPONIBLE, COMPLETO, CANCELADO, FINALIZADO
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Usuario getConductor() { return conductor; }
    public void setConductor(Usuario conductor) { this.conductor = conductor; }
    
    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }
    
    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }
    
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    
    public Integer getAsientosDisponibles() { return asientosDisponibles; }
    public void setAsientosDisponibles(Integer asientosDisponibles) { 
        this.asientosDisponibles = asientosDisponibles; 
    }
    
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}