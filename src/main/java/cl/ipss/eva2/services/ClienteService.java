package cl.ipss.eva2.services;

import cl.ipss.eva2.exceptions.ClienteNotFoundException;
import cl.ipss.eva2.exceptions.ClienteDuplicadoException;
import cl.ipss.eva2.exceptions.ValidacionException;
import cl.ipss.eva2.models.Cliente;
import cl.ipss.eva2.repositories.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // LISTAR TODOS
    public List<Cliente> getAll() {
        return clienteRepository.findAll();
    }

    // OBTENER POR ID
    public Cliente getById(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente no encontrado. ID: " + id));
    }

    // CREAR CLIENTE
    public Cliente create(Cliente cliente) {

        // VALIDAR CAMPOS BÁSICOS
        validarCliente(cliente);

        // VALIDAR DUPLICADOS (RUT, EMAIL O TELÉFONO)
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new ClienteDuplicadoException("Ya existe un cliente con el email: " + cliente.getEmail());
        }

        if (clienteRepository.existsByTelefono(cliente.getTelefono())) {
            throw new ClienteDuplicadoException("Ya existe un cliente con el teléfono: " + cliente.getTelefono());
        }

        return clienteRepository.save(cliente);
    }

    // ACTUALIZAR CLIENTE
    public Cliente update(Long id, Cliente cliente) {

        // VALIDAR EXISTENCIA
        Cliente existente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente no encontrado. ID: " + id));

        // VALIDAR CAMPOS
        validarCliente(cliente);

        // VALIDAR DUPLICADOS (si es otro cliente)
        if (clienteRepository.existsByEmailAndIdNot(cliente.getEmail(), id)) {
            throw new ClienteDuplicadoException("Email ya está registrado por otro cliente.");
        }

        if (clienteRepository.existsByTelefonoAndIdNot(cliente.getTelefono(), id)) {
            throw new ClienteDuplicadoException("Teléfono ya está registrado por otro cliente.");
        }

        // COPIAR CAMPOS A ACTUALIZAR
        existente.setNombre(cliente.getNombre());
        existente.setEmail(cliente.getEmail());
        existente.setTelefono(cliente.getTelefono());

        return clienteRepository.save(existente);
    }

    // ELIMINAR CLIENTE
    public void delete(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ClienteNotFoundException("No se puede eliminar. Cliente no existe. ID: " + id);
        }
        clienteRepository.deleteById(id);
    }

    // ==========================
    // VALIDACIONES BÁSICAS
    // ==========================
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

