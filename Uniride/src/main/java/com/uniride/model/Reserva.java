package com.uniride.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "viaje_id")
    private Viaje viaje;
    
    @ManyToOne
    @JoinColumn(name = "pasajero_id")
    private Usuario pasajero;
    
    private Integer asientosReservados;
    private LocalDateTime fechaReserva;
    private String estado; // PENDIENTE, CONFIRMADA, CANCELADA, COMPLETADA
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Viaje getViaje() { return viaje; }
    public void setViaje(Viaje viaje) { this.viaje = viaje; }
    
    public Usuario getPasajero() { return pasajero; }
    public void setPasajero(Usuario pasajero) { this.pasajero = pasajero; }
    
    public Integer getAsientosReservados() { return asientosReservados; }
    public void setAsientosReservados(Integer asientosReservados) { 
        this.asientosReservados = asientosReservados; 
    }
    
    public LocalDateTime getFechaReserva() { return fechaReserva; }
    public void setFechaReserva(LocalDateTime fechaReserva) { this.fechaReserva = fechaReserva; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}