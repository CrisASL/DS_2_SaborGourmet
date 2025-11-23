package cl.ipss.eva2.services;

import cl.ipss.eva2.models.Mesa;
import cl.ipss.eva2.repositories.MesaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesaService {

    private final MesaRepository mesaRepository;

    public MesaService(MesaRepository mesaRepository) {
        this.mesaRepository = mesaRepository;
    }

    public List<Mesa> getAll() {
        return mesaRepository.findAll();
    }

    public Mesa getById(Long id) {
        return mesaRepository.findById(id).orElse(null);
    }

    public Mesa create(Mesa mesa) {
        return mesaRepository.save(mesa);
    }

    public Mesa update(Long id, Mesa mesa) {
        mesa.setId(id);
        return mesaRepository.save(mesa);
    }

    public void delete(Long id) {
        mesaRepository.deleteById(id);
    }

    public void cambiarDisponibilidad(Long id) {
        Mesa mesa = mesaRepository.findById(id).orElseThrow(() -> new RuntimeException("Mesa no encontrada"));
        mesa.setDisponible(!mesa.isDisponible()); // invierte el estado
        mesaRepository.save(mesa);
    }

    public List<Mesa> getDisponibles() {
    return mesaRepository.findByDisponibleTrue();
}
}
