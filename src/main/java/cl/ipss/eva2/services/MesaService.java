package cl.ipss.eva2.services;

import cl.ipss.eva2.exceptions.MesaNotFoundException;
import cl.ipss.eva2.exceptions.MesaOcupadaException;
import cl.ipss.eva2.exceptions.ValidacionException;
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
        return mesaRepository.findById(id)
                .orElseThrow(() -> new MesaNotFoundException("La mesa con ID " + id + " no existe"));
    }

    public Mesa create(Mesa mesa) {

        if (mesa.getNumero() == null || mesa.getNumero() <= 0) {
            throw new ValidacionException("El número de mesa debe ser mayor a cero");
        }

        if (mesa.getCapacidad() == null || mesa.getCapacidad() <= 0) {
            throw new ValidacionException("La capacidad de la mesa debe ser mayor a cero");
        }

        mesa.setDisponible(true); // Por defecto nueva mesa disponible
        return mesaRepository.save(mesa);
    }

    public Mesa update(Long id, Mesa mesa) {
        Mesa existente = mesaRepository.findById(id)
                .orElseThrow(() -> new MesaNotFoundException("La mesa con ID " + id + " no existe"));

        if (mesa.getNumero() == null || mesa.getNumero() <= 0) {
            throw new ValidacionException("El número de mesa debe ser mayor a cero");
        }

        if (mesa.getCapacidad() == null || mesa.getCapacidad() <= 0) {
            throw new ValidacionException("La capacidad de la mesa debe ser mayor a cero");
        }

        existente.setNumero(mesa.getNumero());
        existente.setCapacidad(mesa.getCapacidad());
        existente.setDisponible(mesa.isDisponible());

        return mesaRepository.save(existente);
    }

    public void delete(Long id) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new MesaNotFoundException("La mesa con ID " + id + " no existe"));

        mesaRepository.delete(mesa);
    }

    public void cambiarDisponibilidad(Long id) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new MesaNotFoundException("La mesa con ID " + id + " no existe"));

        if (!mesa.isDisponible()) {
            throw new MesaOcupadaException("La mesa está ocupada y no puede liberarse automáticamente");
        }

        mesa.setDisponible(!mesa.isDisponible());
        mesaRepository.save(mesa);
    }

    public List<Mesa> getDisponibles() {
        return mesaRepository.findByDisponibleTrue();
    }
}
