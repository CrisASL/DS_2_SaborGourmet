package cl.ipss.eva2.services;

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

    public List<Reserva> getAll() {
        return reservaRepository.findAll();
    }

    public Reserva getById(Long id) {
        return reservaRepository.findById(id).orElse(null);
    }

    public Reserva create(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    public Reserva update(Long id, Reserva reserva) {
        reserva.setId(id);
        return reservaRepository.save(reserva);
    }

    public void delete(Long id) {
        reservaRepository.deleteById(id);
    }
}

