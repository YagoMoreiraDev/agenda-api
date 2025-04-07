package br.gov.ce.direitoshumanos.api_agenda.repositories;

import br.gov.ce.direitoshumanos.api_agenda.models.Agenda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {

    // 1. Buscar reuniões futuras onde o usuário é criador (ordenado pela mais próxima)
    @Query("SELECT a FROM Agenda a " +
            "WHERE a.criador.id = :usuarioId AND a.dataHoraReuniao >= CURRENT_TIMESTAMP " +
            "ORDER BY a.dataHoraReuniao ASC")
    Page<Agenda> buscarReunioesFuturasCriadasPor(@Param("usuarioId") Long usuarioId, Pageable pageable);

    // 2. Buscar reuniões futuras onde o usuário é participante (ordenado pela mais próxima)
    @Query("SELECT a FROM Agenda a " +
            "JOIN a.participantes p " +
            "WHERE p.id = :usuarioId AND a.dataHoraReuniao >= CURRENT_TIMESTAMP " +
            "ORDER BY a.dataHoraReuniao ASC")
    Page<Agenda> buscarReunioesFuturasOndeParticipa(@Param("usuarioId") Long usuarioId, Pageable pageable);

    // 3. Buscar histórico de reuniões já passadas, onde o usuário foi criador e também participou
    @Query("SELECT a FROM Agenda a " +
            "JOIN a.participantes p " +
            "WHERE a.criador.id = :usuarioId AND p.id = :usuarioId AND a.dataHoraReuniao < CURRENT_TIMESTAMP " +
            "ORDER BY a.dataHoraReuniao DESC")
    Page<Agenda> buscarHistoricoDeReunioes(@Param("usuarioId") Long usuarioId, Pageable pageable);

    // 4. Buscar reuniões por local (ex: auditório X)
    List<Agenda> findByLocalId(Long localId);

    // 5. Buscar reuniões por data específica
    List<Agenda> findByDataHoraReuniaoBetween(LocalDateTime start, LocalDateTime end);

    // 6. Buscar por mês específico (exemplo usando JPQL)
    @Query("SELECT a FROM Agenda a WHERE MONTH(a.dataHoraReuniao) = :mes AND YEAR(a.dataHoraReuniao) = :ano")
    List<Agenda> buscarPorMesEAno(@Param("mes") int mes, @Param("ano") int ano);

    // 7. Buscar por dia específico
    @Query("SELECT a FROM Agenda a WHERE DATE(a.dataHoraReuniao) = :data")
    List<Agenda> buscarPorData(@Param("data") LocalDateTime data);

    // 8. Buscar por hora específica
    @Query("SELECT a FROM Agenda a WHERE FUNCTION('HOUR', a.dataHoraReuniao) = :hora")
    List<Agenda> buscarPorHora(@Param("hora") int hora);

    @Query("SELECT a FROM Agenda a LEFT JOIN FETCH a.participantes WHERE a.id = :id")
    Optional<Agenda> buscarPorIdComParticipantes(@Param("id") Long id);

    @Query("""
    SELECT DISTINCT a FROM Agenda a
    LEFT JOIN FETCH a.participantes
    WHERE a.criador.id = :usuarioId AND a.dataHoraReuniao >= CURRENT_TIMESTAMP
    ORDER BY a.dataHoraReuniao ASC
    """)
    List<Agenda> buscarReunioesFuturasCriadasPorFetch(@Param("usuarioId") Long usuarioId);

    @Query("""
    SELECT DISTINCT a FROM Agenda a
    LEFT JOIN FETCH a.participantes p
    WHERE p.id = :usuarioId AND a.dataHoraReuniao >= CURRENT_TIMESTAMP
    ORDER BY a.dataHoraReuniao ASC
    """)
    List<Agenda> buscarReunioesFuturasOndeParticipaFetch(@Param("usuarioId") Long usuarioId);

    @Query("""
    SELECT DISTINCT a FROM Agenda a
    LEFT JOIN FETCH a.participantes p
    WHERE (a.criador.id = :usuarioId OR p.id = :usuarioId)
    AND a.dataHoraReuniao < CURRENT_TIMESTAMP
    ORDER BY a.dataHoraReuniao DESC
    """)
    List<Agenda> buscarHistoricoDeReunioesFetch(@Param("usuarioId") Long usuarioId);

}
