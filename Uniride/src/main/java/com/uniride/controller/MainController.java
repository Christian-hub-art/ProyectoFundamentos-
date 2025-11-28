package com.uniride.controller;

import com.uniride.model.*;
import com.uniride.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ViajeRepository viajeRepository;
    
    @Autowired
    private ReservaRepository reservaRepository;

    // Página principal
    @GetMapping("/")
    public String home(Model model) {
        List<Viaje> viajesDisponibles = viajeRepository.findByEstado("DISPONIBLE");
        model.addAttribute("viajes", viajesDisponibles);
        return "index";
    }

    // === GESTIÓN DE USUARIOS ===
    
    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("usuario", new Usuario());
        return "usuarios";
    }

    @PostMapping("/usuarios")
    public String guardarUsuario(@ModelAttribute Usuario usuario) {
        if (usuario.getEsConductor() == null) {
            usuario.setEsConductor(false);
        }
        usuarioRepository.save(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
        return "redirect:/usuarios";
    }

    // === GESTIÓN DE VIAJES ===
    
    @GetMapping("/viajes")
    public String listarViajes(Model model) {
        model.addAttribute("viajes", viajeRepository.findAll());
        model.addAttribute("viaje", new Viaje());
        model.addAttribute("conductores", usuarioRepository.findByEsConductor(true));
        return "viajes";
    }

    @PostMapping("/viajes")
    public String guardarViaje(@ModelAttribute Viaje viaje, @RequestParam Long conductorId) {
        Usuario conductor = usuarioRepository.findById(conductorId).orElse(null);
        if (conductor != null && conductor.getEsConductor()) {
            viaje.setConductor(conductor);
            if (viaje.getEstado() == null || viaje.getEstado().isEmpty()) {
                viaje.setEstado("DISPONIBLE");
            }
            if (viaje.getFechaHora() == null) {
                viaje.setFechaHora(LocalDateTime.now());
            }
            viajeRepository.save(viaje);
        }
        return "redirect:/viajes";
    }

    @GetMapping("/viajes/eliminar/{id}")
    public String eliminarViaje(@PathVariable Long id) {
        viajeRepository.deleteById(id);
        return "redirect:/viajes";
    }

    @GetMapping("/viajes/cambiar-estado/{id}")
    public String cambiarEstadoViaje(@PathVariable Long id, @RequestParam String nuevoEstado) {
        Viaje viaje = viajeRepository.findById(id).orElse(null);
        if (viaje != null) {
            viaje.setEstado(nuevoEstado);
            viajeRepository.save(viaje);
        }
        return "redirect:/viajes";
    }

    // === GESTIÓN DE RESERVAS ===
    
    @GetMapping("/reservas")
    public String listarReservas(Model model) {
        model.addAttribute("reservas", reservaRepository.findAll());
        model.addAttribute("reserva", new Reserva());
        model.addAttribute("viajes", viajeRepository.findByEstado("DISPONIBLE"));
        model.addAttribute("pasajeros", usuarioRepository.findAll());
        return "reservas";
    }

    @PostMapping("/reservas")
    public String guardarReserva(@ModelAttribute Reserva reserva, 
                                  @RequestParam Long viajeId, 
                                  @RequestParam Long pasajeroId) {
        Viaje viaje = viajeRepository.findById(viajeId).orElse(null);
        Usuario pasajero = usuarioRepository.findById(pasajeroId).orElse(null);
        
        if (viaje != null && pasajero != null) {
            if (viaje.getAsientosDisponibles() >= reserva.getAsientosReservados()) {
                reserva.setViaje(viaje);
                reserva.setPasajero(pasajero);
                reserva.setFechaReserva(LocalDateTime.now());
                reserva.setEstado("CONFIRMADA");
                
                // Actualizar asientos disponibles
                viaje.setAsientosDisponibles(viaje.getAsientosDisponibles() - reserva.getAsientosReservados());
                if (viaje.getAsientosDisponibles() == 0) {
                    viaje.setEstado("COMPLETO");
                }
                viajeRepository.save(viaje);
                reservaRepository.save(reserva);
            }
        }
        return "redirect:/reservas";
    }

    @GetMapping("/reservas/cancelar/{id}")
    public String cancelarReserva(@PathVariable Long id) {
        Reserva reserva = reservaRepository.findById(id).orElse(null);
        if (reserva != null) {
            Viaje viaje = reserva.getViaje();
            viaje.setAsientosDisponibles(viaje.getAsientosDisponibles() + reserva.getAsientosReservados());
            viaje.setEstado("DISPONIBLE");
            viajeRepository.save(viaje);
            
            reserva.setEstado("CANCELADA");
            reservaRepository.save(reserva);
        }
        return "redirect:/reservas";
    }

    @GetMapping("/reservas/eliminar/{id}")
    public String eliminarReserva(@PathVariable Long id) {
        reservaRepository.deleteById(id);
        return "redirect:/reservas";
    }

    // === BÚSQUEDA DE VIAJES ===
    
    @GetMapping("/buscar")
    public String buscarViajes(Model model) {
        model.addAttribute("viajes", viajeRepository.findByEstado("DISPONIBLE"));
        return "buscar";
    }

    @PostMapping("/buscar")
    public String buscarViajesPorRuta(@RequestParam String origen, 
                                       @RequestParam String destino, 
                                       Model model) {
        List<Viaje> viajes = viajeRepository.findByOrigenAndDestino(origen, destino);
        model.addAttribute("viajes", viajes);
        model.addAttribute("origen", origen);
        model.addAttribute("destino", destino);
        return "buscar";
    }
}