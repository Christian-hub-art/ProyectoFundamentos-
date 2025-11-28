package com.uniride.repository;

import com.uniride.model.Viaje;
import com.uniride.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ViajeRepository extends JpaRepository<Viaje, Long> {
    List<Viaje> findByEstado(String estado);
    List<Viaje> findByConductor(Usuario conductor);
    List<Viaje> findByOrigenAndDestino(String origen, String destino);
}