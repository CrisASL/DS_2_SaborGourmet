package cl.ipss.eva2.repositories;

import cl.ipss.eva2.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Aquí puedes agregar consultas personalizadas si las necesitas
    // Ejemplo: Optional<Cliente> findByEmail(String email);
}
