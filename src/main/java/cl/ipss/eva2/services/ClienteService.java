package cl.ipss.eva2.services;

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

    public List<Cliente> getAll() {
        return clienteRepository.findAll();
    }

    public Cliente getById(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    public Cliente create(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente update(Long id, Cliente cliente) {
        cliente.setId(id);
        return clienteRepository.save(cliente);
    }

    public void delete(Long id) {
        clienteRepository.deleteById(id);
    }
}
