package com.uniride.service;

import com.uniride.model.Usuario;
import com.uniride.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.regex.Pattern;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    private static final Pattern PASSWORD_PATTERN = 
        Pattern.compile("^(?=.*[0-9])(?=.*[A-Z]).{8,}$");
    
    /**
     * CU01 - Registrar Usuario
     * Valida y registra un nuevo usuario
     */
    public Usuario registrarUsuario(Usuario usuario, String password) throws Exception {
        // E1 - Verificar si el correo ya está registrado
        Usuario existente = usuarioRepository.findByEmail(usuario.getEmail());
        if (existente != null) {
            throw new Exception("Ya existe una cuenta con este correo institucional");
        }
        
        // E2 - Validar contraseña
        if (!validarPassword(password)) {
            throw new Exception("La contraseña debe tener mínimo 8 caracteres, incluir un número y una letra mayúscula");
        }
        
        // Crear usuario
        return usuarioRepository.save(usuario);
    }
    
    /**
     * CU02 - Iniciar Sesión
     * Valida credenciales del usuario
     */
    public Usuario iniciarSesion(String email, String password) throws Exception {
        Usuario usuario = usuarioRepository.findByEmail(email);
        
        // E1 - Credenciales incorrectas
        if (usuario == null) {
            throw new Exception("Correo o contraseña incorrectos");
        }
        
        // Validar password (en producción usar BCrypt)
        if (!password.equals(usuario.getPassword())) {
            throw new Exception("Correo o contraseña incorrectos");
        }
        
        return usuario;
    }
    
    /**
     * Valida formato de contraseña
     */
    private boolean validarPassword(String password) {
        if (password == null) return false;
        return PASSWORD_PATTERN.matcher(password).matches();
    }
}