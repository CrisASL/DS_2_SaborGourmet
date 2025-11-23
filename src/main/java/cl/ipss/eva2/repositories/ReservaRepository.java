package cl.ipss.eva2.repositories;

import cl.ipss.eva2.models.Cliente;
import cl.ipss.eva2.models.Mesa;
import cl.ipss.eva2.models.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Listar reservas por fecha
    List<Reserva> findByFecha(LocalDate fecha);

    // Listar reservas de un cliente
    List<Reserva> findByClienteId(Long clienteId);

    // Listar reservas de una mesa
    List<Reserva> findByMesaId(Long mesaId);

    // Validaciones de duplicados
    List<Reserva> findByClienteAndFechaAndHora(Cliente cliente, LocalDate fecha, LocalTime hora);

    List<Reserva> findByMesaAndFechaAndHora(Mesa mesa, LocalDate fecha, LocalTime hora);

   List<Reserva> findByFechaAndHora(LocalDate fecha, LocalTime hora);
}


