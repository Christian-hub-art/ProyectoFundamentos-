package com.uniride.repository;

import com.uniride.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findByEsConductor(Boolean esConductor);
    Usuario findByEmail(String email);
}