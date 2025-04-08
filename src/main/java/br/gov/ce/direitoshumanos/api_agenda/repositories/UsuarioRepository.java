package br.gov.ce.direitoshumanos.api_agenda.repositories;

import br.gov.ce.direitoshumanos.api_agenda.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCpf(String cpf);

    List<Usuario> findByNomeStartingWithIgnoreCase(String nome);
}
