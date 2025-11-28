package com.uniride.repository;

import com.uniride.model.Reserva;
import com.uniride.model.Usuario;
import com.uniride.model.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByPasajero(Usuario pasajero);
    List<Reserva> findByViaje(Viaje viaje);
    List<Reserva> findByEstado(String estado);
}