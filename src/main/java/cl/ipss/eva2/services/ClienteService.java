package cl.ipss.eva2.services;

import cl.ipss.eva2.exceptions.ClienteNotFoundException;
import cl.ipss.eva2.exceptions.ClienteDuplicadoException;
import cl.ipss.eva2.exceptions.ValidacionException;
import cl.ipss.eva2.models.Cliente;
import cl.ipss.eva2.repositories.ClienteRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> getAll() {
        return clienteRepository.findAll();
    }

    public Cliente getById(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente no encontrado. ID: " + id));
    }

    @Transactional
    public Cliente create(Cliente cliente) {
        validarCliente(cliente);

        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new ClienteDuplicadoException("Ya existe un cliente con el email: " + cliente.getEmail());
        }

        if (clienteRepository.existsByTelefono(cliente.getTelefono())) {
            throw new ClienteDuplicadoException("Ya existe un cliente con el teléfono: " + cliente.getTelefono());
        }

        Cliente nuevoCliente = clienteRepository.save(cliente);
        clienteRepository.flush(); // Forzar commit inmediato
        return nuevoCliente;
    }

    @Transactional
    public Cliente update(Long id, Cliente cliente) {
        Cliente existente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente no encontrado. ID: " + id));

        validarCliente(cliente);

        if (clienteRepository.existsByEmailAndIdNot(cliente.getEmail(), id)) {
            throw new ClienteDuplicadoException("Email ya está registrado por otro cliente.");
        }

        if (clienteRepository.existsByTelefonoAndIdNot(cliente.getTelefono(), id)) {
            throw new ClienteDuplicadoException("Teléfono ya está registrado por otro cliente.");
        }

        existente.setNombre(cliente.getNombre());
        existente.setEmail(cliente.getEmail());
        existente.setTelefono(cliente.getTelefono());

        Cliente updated = clienteRepository.save(existente);
        clienteRepository.flush(); // Forzar commit inmediato
        return updated;
    }

    @Transactional
    public void delete(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("No se puede eliminar. Cliente no existe. ID: " + id));
        try {
            clienteRepository.delete(cliente);
            clienteRepository.flush(); // Forzar commit y capturar errores de integridad
        } catch (DataIntegrityViolationException e) {
            throw new ValidacionException(
                "No se puede eliminar este cliente porque tiene reservas o registros asociados."
            );
        }
    }

    private void validarCliente(Cliente cliente) {
        if (cliente.getNombre() == null || cliente.getNombre().isBlank()) {
            throw new ValidacionException("El nombre no puede estar vacío.");
        }
        if (cliente.getEmail() == null || cliente.getEmail().isBlank()) {
            throw new ValidacionException("El email es obligatorio.");
        }
        if (cliente.getTelefono() == null || cliente.getTelefono().isBlank()) {
            throw new ValidacionException("El teléfono es obligatorio.");
        }
    }
}

