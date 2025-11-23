package cl.ipss.eva2.repositories;

import cl.ipss.eva2.models.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {

    // Método para obtener solo mesas disponibles
    List<Mesa> findByDisponibleTrue();

    Optional<Mesa> findByNumero(Integer numero);

    boolean existsByNumero(Integer numero);
    boolean existsByNumeroAndIdNot(Integer numero, Long id);


}
