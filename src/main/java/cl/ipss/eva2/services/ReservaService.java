package cl.ipss.eva2.services;

import cl.ipss.eva2.exceptions.ValidacionException;
import cl.ipss.eva2.models.Reserva;
import cl.ipss.eva2.repositories.ReservaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    // LISTAR TODAS LAS RESERVAS
    public List<Reserva> getAll() {
        return reservaRepository.findAll();
    }

    // OBTENER RESERVA POR ID
    public Reserva getById(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new ValidacionException("Reserva no encontrada. ID: " + id));
    }

    // CREAR RESERVA
    public Reserva create(Reserva reserva) {

        // Validar duplicado de cliente
        boolean clienteDuplicado = !reservaRepository.findByClienteAndFechaAndHora(
                reserva.getCliente(), reserva.getFecha(), reserva.getHora()
        ).isEmpty();

        if (clienteDuplicado) {
            throw new ValidacionException("El cliente ya tiene una reserva para esa fecha y hora.");
        }

        // Validar duplicado de mesa
        boolean mesaDuplicada = !reservaRepository.findByMesaAndFechaAndHora(
                reserva.getMesa(), reserva.getFecha(), reserva.getHora()
        ).isEmpty();

        if (mesaDuplicada) {
            throw new ValidacionException("La mesa ya está reservada para esa fecha y hora.");
        }

        reserva.setEstado(true); // activa por defecto
        return reservaRepository.save(reserva);
    }

    // ACTUALIZAR RESERVA
    public Reserva update(Long id, Reserva reserva) {
        Reserva existente = reservaRepository.findById(id)
                .orElseThrow(() -> new ValidacionException("Reserva no encontrada. ID: " + id));

        // Validar duplicado de cliente (ignorando la propia reserva)
        boolean clienteDuplicado = reservaRepository.findByClienteAndFechaAndHora(
                reserva.getCliente(), reserva.getFecha(), reserva.getHora()
        ).stream().anyMatch(r -> !r.getId().equals(id));

        if (clienteDuplicado) {
            throw new ValidacionException("El cliente ya tiene una reserva para esa fecha y hora.");
        }

        // Validar duplicado de mesa (ignorando la propia reserva)
        boolean mesaDuplicada = reservaRepository.findByMesaAndFechaAndHora(
                reserva.getMesa(), reserva.getFecha(), reserva.getHora()
        ).stream().anyMatch(r -> !r.getId().equals(id));

        if (mesaDuplicada) {
            throw new ValidacionException("La mesa ya está reservada para esa fecha y hora.");
        }

        // Actualizar campos
        existente.setCliente(reserva.getCliente());
        existente.setMesa(reserva.getMesa());
        existente.setFecha(reserva.getFecha());
        existente.setHora(reserva.getHora());
        existente.setEstado(reserva.getEstado());

        return reservaRepository.save(existente);
    }

    // ELIMINAR RESERVA
    public void delete(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ValidacionException("Reserva no encontrada. ID: " + id));
        reservaRepository.delete(reserva);
    }
}





