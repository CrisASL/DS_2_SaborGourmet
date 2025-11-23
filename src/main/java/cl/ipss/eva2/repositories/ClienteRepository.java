package cl.ipss.eva2.repositories;

import cl.ipss.eva2.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Validar duplicados al crear
    boolean existsByEmail(String email);
    boolean existsByTelefono(String telefono);

    // Validar duplicados al editar (excluyendo el propio id)
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByTelefonoAndIdNot(String telefono, Long id);
}
