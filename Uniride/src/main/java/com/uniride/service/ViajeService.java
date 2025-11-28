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
public class ViajeService {
    
    @Autowired
    private ViajeRepository viajeRepository;
    
    @Autowired
    private ReservaRepository reservaRepository;
    
    /**
     * CU03 - Publicar Viaje
     * Crea un nuevo viaje con validaciones
     */
    public Viaje publicarViaje(Viaje viaje) throws Exception {
        // E1 - Validar fecha/hora futuras
        if (viaje.getFechaHora() == null || 
            viaje.getFechaHora().isBefore(LocalDateTime.now())) {
            throw new Exception("La fecha u hora no son válidas");
        }
        
        // E2 - Validar asientos
        if (viaje.getAsientosDisponibles() == null || 
            viaje.getAsientosDisponibles() < 1) {
            throw new Exception("Debe ingresar mínimo 1 asiento disponible");
        }
        
        // Establecer estado inicial
        viaje.setEstado("DISPONIBLE");
        
        return viajeRepository.save(viaje);
    }
    
    /**
     * CU04 - Buscar Viaje
     * Busca viajes disponibles según origen y destino
     */
    public List<Viaje> buscarViajes(String origen, String destino) {
        List<Viaje> viajes;
        
        if (origen != null && destino != null && 
            !origen.isEmpty() && !destino.isEmpty()) {
            viajes = viajeRepository.findByOrigenAndDestino(origen, destino);
        } else {
            viajes = viajeRepository.findByEstado("DISPONIBLE");
        }
        
        // E1 - Filtrar solo disponibles
        viajes.removeIf(v -> !"DISPONIBLE".equals(v.getEstado()));
        
        return viajes;
    }
    
    /**
     * CU07 - Cancelar Viaje
     * Cancela un viaje y todas sus reservas
     */
    @Transactional
    public void cancelarViaje(Long viajeId) throws Exception {
        Viaje viaje = viajeRepository.findById(viajeId)
            .orElseThrow(() -> new Exception("Viaje no encontrado"));
        
        // E1 - Verificar que no esté finalizado o ya pasó la fecha
        if ("FINALIZADO".equals(viaje.getEstado())) {
            throw new Exception("No se puede cancelar un viaje finalizado");
        }
        
        if (viaje.getFechaHora().isBefore(LocalDateTime.now())) {
            throw new Exception("No se puede cancelar un viaje que ya ocurrió");
        }
        
        // Cambiar estado del viaje
        viaje.setEstado("CANCELADO");
        viajeRepository.save(viaje);
        
        // Cancelar todas las reservas asociadas
        List<Reserva> reservas = reservaRepository.findByViaje(viaje);
        for (Reserva reserva : reservas) {
            reserva.setEstado("CANCELADA");
            reservaRepository.save(reserva);
        }
    }
    
    /**
     * CU08 - Completar Viaje
     * Marca un viaje como finalizado
     */
    @Transactional
    public void completarViaje(Long viajeId) throws Exception {
        Viaje viaje = viajeRepository.findById(viajeId)
            .orElseThrow(() -> new Exception("Viaje no encontrado"));
        
        // E1 - Verificar que la fecha ya haya pasado
        if (viaje.getFechaHora().isAfter(LocalDateTime.now())) {
            throw new Exception("Este viaje aún no puede finalizarse");
        }
        
        // Verificar que no esté cancelado
        if ("CANCELADO".equals(viaje.getEstado())) {
            throw new Exception("No se puede finalizar un viaje cancelado");
        }
        
        // Cambiar estado del viaje
        viaje.setEstado("FINALIZADO");
        viajeRepository.save(viaje);
        
        // Marcar reservas como completadas
        List<Reserva> reservas = reservaRepository.findByViaje(viaje);
        for (Reserva reserva : reservas) {
            if ("CONFIRMADA".equals(reserva.getEstado())) {
                reserva.setEstado("COMPLETADA");
                reservaRepository.save(reserva);
            }
        }
    }
    
    /**
     * CU10 - Ver Mis Viajes
     * Obtiene viajes de un conductor
     */
    public List<Viaje> obtenerViajesConductor(Usuario conductor) {
        return viajeRepository.findByConductor(conductor);
    }
}