package com.uniride.controller;

import com.uniride.model.Usuario;
import com.uniride.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    // ===== CU02 - INICIAR SESIÓN =====
    
    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String iniciarSesion(@RequestParam String email,
                               @RequestParam String password,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        try {
            // Validar credenciales
            Usuario usuario = usuarioService.iniciarSesion(email, password);
            
            // Guardar usuario en sesión
            session.setAttribute("usuarioLogueado", usuario);
            
            // Mensaje de bienvenida
            redirectAttributes.addFlashAttribute("mensaje", 
                "¡Bienvenido " + usuario.getNombreCompleto() + "!");
            
            return "redirect:/dashboard";
            
        } catch (Exception e) {
            // E1 - Credenciales incorrectas
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        }
    }

    // ===== CU01 - REGISTRAR USUARIO =====
    
    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute Usuario usuario,
                                   @RequestParam String password,
                                   RedirectAttributes redirectAttributes) {
        try {
            // Asegurar que esConductor no sea null
            if (usuario.getEsConductor() == null) {
                usuario.setEsConductor(false);
            }
            
            // Guardar password en el usuario
            usuario.setPassword(password);
            
            // Registrar con validaciones
            usuarioService.registrarUsuario(usuario, password);
            
            // Mensaje de éxito
            redirectAttributes.addFlashAttribute("mensaje", 
                "¡Cuenta creada exitosamente! Ya puedes iniciar sesión");
            
            return "redirect:/login";
            
        } catch (Exception e) {
            // E1 o E2 - Errores de validación
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/registro";
        }
    }

    // ===== CERRAR SESIÓN =====
    
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("mensaje", "Sesión cerrada correctamente");
        return "redirect:/login";
    }

    // ===== DASHBOARD (después de login) =====
    
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        
        // Si no hay sesión, redirigir a login
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", 
                "Debes iniciar sesión primero");
            return "redirect:/login";
        }
        
        model.addAttribute("usuario", usuario);
        return "dashboard";
    }
}