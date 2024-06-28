package chiarafais.GestioneDispositivi.repositories;

import chiarafais.GestioneDispositivi.entities.Dispositivo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DispositiviDAO extends JpaRepository<Dispositivo, Integer> {
}
