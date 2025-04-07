package br.gov.ce.direitoshumanos.api_agenda.services;

import br.gov.ce.direitoshumanos.api_agenda.dtos.AgendaDTO;
import br.gov.ce.direitoshumanos.api_agenda.dtos.AgendaResponseDTO;
import br.gov.ce.direitoshumanos.api_agenda.dtos.ParticipanteDTO;
import br.gov.ce.direitoshumanos.api_agenda.enums.TipoAgenda;
import br.gov.ce.direitoshumanos.api_agenda.models.Agenda;
import br.gov.ce.direitoshumanos.api_agenda.models.Local;
import br.gov.ce.direitoshumanos.api_agenda.models.Usuario;
import br.gov.ce.direitoshumanos.api_agenda.repositories.AgendaRepository;
import br.gov.ce.direitoshumanos.api_agenda.repositories.LocalRepository;
import br.gov.ce.direitoshumanos.api_agenda.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AgendaService {

    private final AgendaRepository agendaRepository;
    private final UsuarioRepository usuarioRepository;
    private final LocalRepository localRepository;

    /**
     * Reuniões futuras criadas pelo usuário
     */
    public Page<Agenda> listarReunioesFuturasCriadasPor(Long usuarioId, int pagina) {
        Pageable pageable = PageRequest.of(pagina, 10); // 10 por página
        return agendaRepository.buscarReunioesFuturasCriadasPor(usuarioId, pageable);
    }

    /**
     * Reuniões futuras onde o usuário participa
     */
    public Page<Agenda> listarReunioesFuturasOndeParticipa(Long usuarioId, int pagina) {
        Pageable pageable = PageRequest.of(pagina, 10);
        return agendaRepository.buscarReunioesFuturasOndeParticipa(usuarioId, pageable);
    }

    /**
     * Histórico de reuniões já passadas onde o usuário foi criador e participou
     */
    public Page<Agenda> listarHistoricoDeReunioesDoUsuario(Long usuarioId, int pagina) {
        Pageable pageable = PageRequest.of(pagina, 10);
        return agendaRepository.buscarHistoricoDeReunioes(usuarioId, pageable);
    }

    /**
     * Buscar por ID
     */
    public Agenda buscarPorId(Long id) {
        return agendaRepository.buscarPorIdComParticipantes(id)
                .orElseThrow(() -> new RuntimeException("Agenda não encontrada"));
    }

    /**
     * Criar uma nova reunião
     */
    public Agenda salvar(AgendaDTO dto) {
        Usuario criador = usuarioRepository.findById(dto.getCriadorId())
                .orElseThrow(() -> new RuntimeException("Criador não encontrado"));

        Local local = localRepository.findById(dto.getLocalId())
                .orElseThrow(() -> new RuntimeException("Local não encontrado"));

        // Validação condicional: reunião exige participantes
        if (dto.getTipo() == TipoAgenda.REUNIAO &&
                (dto.getParticipantesIds() == null || dto.getParticipantesIds().isEmpty())) {
            throw new RuntimeException("Reuniões devem ter pelo menos um participante");
        }

        List<Usuario> participantes = (dto.getParticipantesIds() != null)
                ? usuarioRepository.findAllById(dto.getParticipantesIds())
                : List.of();

        Agenda agenda = new Agenda();
        agenda.setCriador(criador);
        agenda.setLocal(local);
        agenda.setParticipantes(participantes);
        agenda.setDataHoraReuniao(dto.getDataHoraReuniao());
        agenda.setAssunto(dto.getAssunto());
        agenda.setTipo(dto.getTipo());

        return agendaRepository.save(agenda);
    }

    /**
     * Remover reunião por ID
     */
    public void deletar(Long id) {
        Agenda agenda = buscarPorId(id);
        agendaRepository.delete(agenda);
    }

    /**
     * Atualizar dados da agenda
     */
    public Agenda atualizar(Long id, AgendaDTO dto) {
        Agenda existente = buscarPorId(id);

        existente.setDataHoraReuniao(dto.getDataHoraReuniao());
        existente.setAssunto(dto.getAssunto());

        Local local = localRepository.findById(dto.getLocalId())
                .orElseThrow(() -> new RuntimeException("Local não encontrado"));
        existente.setLocal(local);

        List<Usuario> participantes = (dto.getParticipantesIds() != null)
                ? usuarioRepository.findAllById(dto.getParticipantesIds())
                : List.of();
        existente.setParticipantes(participantes);

        existente.setTipo(dto.getTipo());

        return agendaRepository.save(existente);
    }

    public Page<AgendaResponseDTO> listarReunioesFuturasCriadasPorDTO(Long usuarioId, int pagina) {
        Pageable pageable = PageRequest.of(pagina, 10);

        // Busca com JOIN FETCH e remove duplicados com LinkedHashSet
        Set<Agenda> agendasUnicas = new LinkedHashSet<>(agendaRepository.buscarReunioesFuturasCriadasPorFetch(usuarioId));
        List<Agenda> lista = new ArrayList<>(agendasUnicas);

        // Paginação manual
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), lista.size());

        List<AgendaResponseDTO> pageContent = lista.subList(start, end)
                .stream()
                .map(this::toDTO)
                .toList();

        return new PageImpl<>(pageContent, pageable, lista.size());
    }

    public Page<AgendaResponseDTO> listarReunioesFuturasOndeParticipaDTO(Long usuarioId, int pagina) {
        Pageable pageable = PageRequest.of(pagina, 10);

        Set<Agenda> agendasUnicas = new LinkedHashSet<>(agendaRepository.buscarReunioesFuturasOndeParticipaFetch(usuarioId));
        List<Agenda> lista = new ArrayList<>(agendasUnicas);

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), lista.size());

        List<AgendaResponseDTO> pageContent = lista.subList(start, end)
                .stream()
                .map(this::toDTO)
                .toList();

        return new PageImpl<>(pageContent, pageable, lista.size());
    }

    public Page<AgendaResponseDTO> listarHistoricoDeReunioesDoUsuarioDTO(Long usuarioId, int pagina) {
        Pageable pageable = PageRequest.of(pagina, 10);

        // Elimina duplicações
        Set<Agenda> agendasUnicas = new LinkedHashSet<>(agendaRepository.buscarHistoricoDeReunioesFetch(usuarioId));
        List<Agenda> lista = new ArrayList<>(agendasUnicas);

        // Paginação manual
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), lista.size());

        List<AgendaResponseDTO> pageContent = lista.subList(start, end)
                .stream()
                .map(this::toDTO)
                .toList();

        return new PageImpl<>(pageContent, pageable, lista.size());
    }

    public AgendaResponseDTO toDTO(Agenda agenda) {
        AgendaResponseDTO dto = new AgendaResponseDTO();
        dto.setId(agenda.getId());
        dto.setAssunto(agenda.getAssunto());
        dto.setDataHoraReuniao(agenda.getDataHoraReuniao());
        dto.setTipo(agenda.getTipo());

        dto.setCriadorId(agenda.getCriador().getId());
        dto.setCriadorNome(agenda.getCriador().getNome());

        dto.setLocalId(agenda.getLocal().getId());
        dto.setLocalNome(agenda.getLocal().getNome());

        dto.setParticipantes(
                agenda.getParticipantes().stream()
                        .map(p -> new ParticipanteDTO(p.getId(), p.getNome()))
                        .toList()
        );

        return dto;
    }
}

