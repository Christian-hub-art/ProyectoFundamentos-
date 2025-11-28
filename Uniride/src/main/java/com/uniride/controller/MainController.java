package com.uniride.controller;

import com.uniride.model.*;
import com.uniride.repository.*;
import com.uniride.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private ViajeService viajeService;
    
    @Autowired
    private ReservaService reservaService;

    // ===== PÁGINA PRINCIPAL =====
    
    @GetMapping("/")
    public String home(Model model) {
        List<Viaje> viajesDisponibles = viajeRepository.findByEstado("DISPONIBLE");
        model.addAttribute("viajes", viajesDisponibles);
        return "index";
    }

    // ===== CU01 - REGISTRAR USUARIO =====
    
    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("usuario", new Usuario());
        return "usuarios";
    }

    @PostMapping("/usuarios")
    public String guardarUsuario(@ModelAttribute Usuario usuario, 
                                 @RequestParam(required = false) String password,
                                 RedirectAttributes redirectAttributes) {
        try {
            // Validación de contraseña (simulada para este ejemplo)
            if (password == null || password.isEmpty()) {
                password = "Password123"; // Password por defecto para testing
            }
            
            // CU01 - Registrar con validaciones
            if (usuario.getEsConductor() == null) {
                usuario.setEsConductor(false);
            }
            
            usuarioService.registrarUsuario(usuario, password);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario registrado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        
        return "redirect:/usuarios";
    }

    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar usuario");
        }
        return "redirect:/usuarios";
    }

    // ===== CU03 - PUBLICAR VIAJE =====
    
    @GetMapping("/viajes")
    public String listarViajes(Model model) {
        model.addAttribute("viajes", viajeRepository.findAll());
        model.addAttribute("viaje", new Viaje());
        model.addAttribute("conductores", usuarioRepository.findByEsConductor(true));
        return "viajes";
    }

    @PostMapping("/viajes")
    public String guardarViaje(@ModelAttribute Viaje viaje, 
                               @RequestParam Long conductorId,
                               RedirectAttributes redirectAttributes) {
        try {
            Usuario conductor = usuarioRepository.findById(conductorId)
                .orElseThrow(() -> new Exception("Conductor no encontrado"));
            
            if (!conductor.getEsConductor()) {
                throw new Exception("El usuario seleccionado no es conductor");
            }
            
            viaje.setConductor(conductor);
            
            // CU03 - Publicar con validaciones
            viajeService.publicarViaje(viaje);
            
            redirectAttributes.addFlashAttribute("mensaje", "Viaje publicado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        
        return "redirect:/viajes";
    }

    // ===== CU07 - CANCELAR VIAJE =====
    
    @GetMapping("/viajes/cancelar/{id}")
    public String cancelarViaje(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            viajeService.cancelarViaje(id);
            redirectAttributes.addFlashAttribute("mensaje", "Viaje cancelado. Todas las reservas fueron anuladas.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/viajes";
    }
    
    // ===== CU08 - COMPLETAR VIAJE =====
    
    @GetMapping("/viajes/finalizar/{id}")
    public String finalizarViaje(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            viajeService.completarViaje(id);
            redirectAttributes.addFlashAttribute("mensaje", "Viaje finalizado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/viajes";
    }

    @GetMapping("/viajes/eliminar/{id}")
    public String eliminarViaje(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            viajeRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("mensaje", "Viaje eliminado");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar viaje");
        }
        return "redirect:/viajes";
    }

    // ===== CU04 - BUSCAR VIAJE =====
    
    @GetMapping("/buscar")
    public String buscarViajes(Model model) {
        List<Viaje> viajes = viajeService.buscarViajes(null, null);
        model.addAttribute("viajes", viajes);
        return "buscar";
    }

    @PostMapping("/buscar")
    public String buscarViajesPorRuta(@RequestParam(required = false) String origen, 
                                      @RequestParam(required = false) String destino, 
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        try {
            List<Viaje> viajes = viajeService.buscarViajes(origen, destino);
            
            model.addAttribute("viajes", viajes);
            model.addAttribute("origen", origen);
            model.addAttribute("destino", destino);
            
            // E1 - No existen viajes
            if (viajes.isEmpty()) {
                model.addAttribute("mensaje", "No hay viajes disponibles para esta ruta");
            }
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "buscar";
    }

    // ===== CU05 - RESERVAR ASIENTO =====
    
    @GetMapping("/reservas")
    public String listarReservas(Model model) {
        model.addAttribute("reservas", reservaRepository.findAll());
        model.addAttribute("reserva", new Reserva());
        model.addAttribute("viajes", viajeRepository.findByEstado("DISPONIBLE"));
        model.addAttribute("pasajeros", usuarioRepository.findAll());
        return "reservas";
    }

    @PostMapping("/reservas")
    public String guardarReserva(@RequestParam Long viajeId, 
                                 @RequestParam Long pasajeroId,
                                 @RequestParam Integer asientosReservados,
                                 RedirectAttributes redirectAttributes) {
        try {
            Viaje viaje = viajeRepository.findById(viajeId)
                .orElseThrow(() -> new Exception("Viaje no encontrado"));
            
            Usuario pasajero = usuarioRepository.findById(pasajeroId)
                .orElseThrow(() -> new Exception("Pasajero no encontrado"));
            
            // CU05 - Reservar con validaciones
            reservaService.reservarAsiento(viaje, pasajero, asientosReservados);
            
            redirectAttributes.addFlashAttribute("mensaje", "Reserva confirmada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        
        return "redirect:/reservas";
    }

    // ===== CU06 - CANCELAR RESERVA =====
    
    @GetMapping("/reservas/cancelar/{id}")
    public String cancelarReserva(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reservaService.cancelarReserva(id);
            redirectAttributes.addFlashAttribute("mensaje", "Reserva cancelada. Los asientos fueron restaurados.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/reservas";
    }

    @GetMapping("/reservas/eliminar/{id}")
    public String eliminarReserva(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reservaRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("mensaje", "Reserva eliminada");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar reserva");
        }
        return "redirect:/reservas";
    }
    
    // ===== CU09 y CU10 - VER MIS RESERVAS/VIAJES (Por pasajero/conductor específico) =====
    
    @GetMapping("/mis-reservas/{pasajeroId}")
    public String verMisReservas(@PathVariable Long pasajeroId, Model model) {
        Usuario pasajero = usuarioRepository.findById(pasajeroId).orElse(null);
        if (pasajero != null) {
            List<Reserva> reservas = reservaService.obtenerReservasPasajero(pasajero);
            model.addAttribute("reservas", reservas);
            model.addAttribute("pasajero", pasajero);
        }
        return "mis-reservas";
    }
    
    @GetMapping("/mis-viajes/{conductorId}")
    public String verMisViajes(@PathVariable Long conductorId, Model model) {
        Usuario conductor = usuarioRepository.findById(conductorId).orElse(null);
        if (conductor != null && conductor.getEsConductor()) {
            List<Viaje> viajes = viajeService.obtenerViajesConductor(conductor);
            model.addAttribute("viajes", viajes);
            model.addAttribute("conductor", conductor);
        }
        return "mis-viajes";
    }
}