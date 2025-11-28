package com.uniride.service;

import com.uniride.model.Reserva;
import com.uniride.model.Usuario;
import com.uniride.model.Viaje;
import com.uniride.repository.ReservaRepository;
import com.uniride.repository.ViajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservaService {
    
    @Autowired
    private ReservaRepository reservaRepository;
    
    @Autowired
    private ViajeRepository viajeRepository;
    
    /**
     * CU05 - Reservar Asiento
     * Crea una reserva con validaciones
     */
    @Transactional
    public Reserva reservarAsiento(Viaje viaje, Usuario pasajero, Integer asientos) throws Exception {
        // Verificar que el viaje existe
        viaje = viajeRepository.findById(viaje.getId())
            .orElseThrow(() -> new Exception("Viaje no encontrado"));
        
        // E2 - Verificar que el viaje no esté cancelado
        if ("CANCELADO".equals(viaje.getEstado())) {
            throw new Exception("Este viaje ya no está disponible");
        }
        
        // Verificar que el viaje no haya sido finalizado
        if ("FINALIZADO".equals(viaje.getEstado())) {
            throw new Exception("Este viaje ya fue finalizado");
        }
        
        // E1 - Verificar disponibilidad de asientos
        if (viaje.getAsientosDisponibles() < asientos) {
            throw new Exception("No hay asientos disponibles suficientes");
        }
        
        // Crear reserva
        Reserva reserva = new Reserva();
        reserva.setViaje(viaje);
        reserva.setPasajero(pasajero);
        reserva.setAsientosReservados(asientos);
        reserva.setFechaReserva(LocalDateTime.now());
        reserva.setEstado("CONFIRMADA");
        
        // Actualizar asientos disponibles
        viaje.setAsientosDisponibles(viaje.getAsientosDisponibles() - asientos);
        
        // Si no quedan asientos, marcar viaje como completo
        if (viaje.getAsientosDisponibles() == 0) {
            viaje.setEstado("COMPLETO");
        }
        
        viajeRepository.save(viaje);
        return reservaRepository.save(reserva);
    }
    
    /**
     * CU06 - Cancelar Reserva
     * Cancela una reserva y restaura asientos
     */
    @Transactional
    public void cancelarReserva(Long reservaId) throws Exception {
        Reserva reserva = reservaRepository.findById(reservaId)
            .orElseThrow(() -> new Exception("Reserva no encontrada"));
        
        // E1 - Verificar que no esté completada
        if ("COMPLETADA".equals(reserva.getEstado())) {
            throw new Exception("No se puede cancelar una reserva completada");
        }
        
        // Verificar que no esté ya cancelada
        if ("CANCELADA".equals(reserva.getEstado())) {
            throw new Exception("Esta reserva ya está cancelada");
        }
        
        // Restaurar asientos al viaje
        Viaje viaje = reserva.getViaje();
        viaje.setAsientosDisponibles(
            viaje.getAsientosDisponibles() + reserva.getAsientosReservados()
        );
        
        // Si el viaje estaba completo, volver a disponible
        if ("COMPLETO".equals(viaje.getEstado())) {
            viaje.setEstado("DISPONIBLE");
        }
        
        viajeRepository.save(viaje);
        
        // Cambiar estado de la reserva
        reserva.setEstado("CANCELADA");
        reservaRepository.save(reserva);
    }
    
    /**
     * CU09 - Ver Mis Reservas
     * Obtiene las reservas de un pasajero
     */
    public List<Reserva> obtenerReservasPasajero(Usuario pasajero) {
        return reservaRepository.findByPasajero(pasajero);
    }
    
    /**
     * Obtiene todas las reservas (para administración)
     */
    public List<Reserva> obtenerTodasReservas() {
        return reservaRepository.findAll();
    }
}