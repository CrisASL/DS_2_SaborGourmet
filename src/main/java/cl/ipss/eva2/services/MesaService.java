package cl.ipss.eva2.services;

import cl.ipss.eva2.exceptions.DuplicadoException;
import cl.ipss.eva2.exceptions.MesaNotFoundException;
import cl.ipss.eva2.exceptions.MesaOcupadaException;
import cl.ipss.eva2.exceptions.ValidacionException;
import cl.ipss.eva2.models.Mesa;
import cl.ipss.eva2.repositories.MesaRepository;
import cl.ipss.eva2.repositories.ReservaRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MesaService {

    private final MesaRepository mesaRepository;
    private final ReservaRepository reservaRepository;

    public MesaService(MesaRepository mesaRepository, ReservaRepository reservaRepository) {
        this.mesaRepository = mesaRepository;
        this.reservaRepository = reservaRepository;
    }

    public List<Mesa> getAll() {
        return mesaRepository.findAll();
    }

    public Mesa getById(Long id) {
        return mesaRepository.findById(id)
                .orElseThrow(() -> new MesaNotFoundException("La mesa con ID " + id + " no existe"));
    }

    @Transactional
    public Mesa create(Mesa mesa) {
        // Validaciones básicas
        if (mesa.getNumero() == null || mesa.getNumero() <= 0) {
            throw new ValidacionException("El número de mesa debe ser mayor a cero");
        }

        if (mesa.getCapacidad() == null || mesa.getCapacidad() <= 0) {
            throw new ValidacionException("La capacidad de la mesa debe ser mayor a cero");
        }

        // Validar duplicados
        if (mesaRepository.existsByNumero(mesa.getNumero())) {
            throw new DuplicadoException("Ya existe una mesa con el número: " + mesa.getNumero());
        }

        mesa.setDisponible(true); // Por defecto, nueva mesa disponible
        Mesa nuevaMesa = mesaRepository.save(mesa);
        mesaRepository.flush(); // Fuerza la sincronización inmediata con la DB
        return nuevaMesa;
    }

    @Transactional
    public Mesa update(Long id, Mesa mesa) {
        Mesa existente = mesaRepository.findById(id)
                .orElseThrow(() -> new MesaNotFoundException("La mesa con ID " + id + " no existe"));

        // Validaciones básicas
        if (mesa.getNumero() == null || mesa.getNumero() <= 0) {
            throw new ValidacionException("El número de mesa debe ser mayor a cero");
        }

        if (mesa.getCapacidad() == null || mesa.getCapacidad() <= 0) {
            throw new ValidacionException("La capacidad de la mesa debe ser mayor a cero");
        }

        // Validar duplicados en actualización (excepto la misma mesa)
        if (mesaRepository.existsByNumeroAndIdNot(mesa.getNumero(), id)) {
            throw new DuplicadoException("Ya existe otra mesa con el número: " + mesa.getNumero());
        }

        existente.setNumero(mesa.getNumero());
        existente.setCapacidad(mesa.getCapacidad());
        existente.setDisponible(mesa.isDisponible());

        Mesa updated = mesaRepository.save(existente);
        mesaRepository.flush(); // fuerza commit inmediato
        return updated;
    }

    @Transactional
    public void delete(Long id) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new MesaNotFoundException("La mesa con ID " + id + " no existe"));

        try {
            mesaRepository.delete(mesa);
            mesaRepository.flush(); // fuerza commit inmediato y captura problemas de integridad
        } catch (DataIntegrityViolationException e) {
            throw new ValidacionException(
                "No se puede eliminar esta mesa porque tiene reservas asociadas."
            );
        }
    }

    @Transactional
    public void cambiarDisponibilidad(Long id) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new MesaNotFoundException("La mesa con ID " + id + " no existe"));

        if (!mesa.isDisponible()) {
            throw new MesaOcupadaException("La mesa está ocupada y no puede liberarse automáticamente");
        }

        mesa.setDisponible(!mesa.isDisponible());
        mesaRepository.save(mesa);
        mesaRepository.flush();
    }

    public List<Mesa> getDisponibles() {
        return mesaRepository.findByDisponibleTrue();
    }

    // Mesas disponibles para una fecha y hora específica
    public List<Mesa> getDisponiblesPara(LocalDate fecha, LocalTime hora) {
        List<Long> mesasOcupadas = reservaRepository.findByFechaAndHora(fecha, hora)
                .stream()
                .map(r -> r.getMesa().getId())
                .collect(Collectors.toList());

        if (mesasOcupadas.isEmpty()) {
            return mesaRepository.findAll(); // todas disponibles
        }

        return mesaRepository.findAll()
                .stream()
                .filter(m -> !mesasOcupadas.contains(m.getId()))
                .collect(Collectors.toList());
    }
}


