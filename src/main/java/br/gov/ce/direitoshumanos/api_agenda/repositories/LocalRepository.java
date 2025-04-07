package br.gov.ce.direitoshumanos.api_agenda.repositories;

import br.gov.ce.direitoshumanos.api_agenda.models.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocalRepository extends JpaRepository<Local, Long> {

    Optional<Local> findByNome(String nome);
}
