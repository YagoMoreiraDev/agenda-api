package br.gov.ce.direitoshumanos.api_agenda.services;

import br.gov.ce.direitoshumanos.api_agenda.dtos.AgendaDTO;
import br.gov.ce.direitoshumanos.api_agenda.dtos.AgendaResponseDTO;
import br.gov.ce.direitoshumanos.api_agenda.dtos.ParticipanteDTO;
import br.gov.ce.direitoshumanos.api_agenda.enums.StatusEnum;
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

        // ✅ Se não foi informado um localId, usa o ID da sala padrão (1)
        Long localId = (dto.getLocalId() == null || dto.getLocalId() == 0) ? 1L : dto.getLocalId();

        Local local = localRepository.findById(localId)
                .orElseThrow(() -> new RuntimeException("Local não encontrado"));

        // ✅ Verifica conflito no local + dataHora
        if (agendaRepository.existsByLocalIdAndDataHoraReuniao(localId, dto.getDataHoraReuniao())) {
            throw new RuntimeException("Já existe um agendamento neste local, data e hora.");
        }

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
        agenda.setStatus(StatusEnum.PENDENTE);

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
        Pageable pageable = PageRequest.of(pagina, 4);

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

    public Page<AgendaResponseDTO> listarPorStatus(StatusEnum status, Long usuarioId, int pagina) {
        Pageable pageable = PageRequest.of(pagina, 10);
        Set<Agenda> agendasUnicas = new LinkedHashSet<>(
                agendaRepository.buscarPorStatus(status, usuarioId)
        );

        List<Agenda> lista = new ArrayList<>(agendasUnicas);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), lista.size());

        List<AgendaResponseDTO> pageContent = lista.subList(start, end)
                .stream()
                .map(this::toDTO)
                .toList();

        return new PageImpl<>(pageContent, pageable, lista.size());
    }

    /**
     * Atualiza o status de uma agenda existente.
     * Apenas participantes devem poder fazer isso.
     */
    public Agenda atualizarStatus(Long agendaId, StatusEnum novoStatus) {
        Agenda agenda = buscarPorId(agendaId);
        agenda.setStatus(novoStatus);
        return agendaRepository.save(agenda);
    }

    public List<AgendaResponseDTO> listarReunioesConfirmadasDoUsuario(Long usuarioId) {
        Set<Agenda> confirmadas = new LinkedHashSet<>();
        confirmadas.addAll(agendaRepository.buscarConfirmadasCriadasPor(usuarioId));
        confirmadas.addAll(agendaRepository.buscarConfirmadasOndeParticipa(usuarioId));

        return confirmadas.stream()
                .map(this::toDTO)
                .toList();
    }

    public Page<AgendaResponseDTO> listarConfirmadasFuturas(Long usuarioId, int pagina) {
        Pageable pageable = PageRequest.of(pagina, 8); // ✅ 8 por página

        Set<Agenda> agendas = new LinkedHashSet<>(
                agendaRepository.buscarConfirmadasFuturasPorUsuario(usuarioId)
        );

        List<Agenda> lista = new ArrayList<>(agendas);

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), lista.size());

        List<AgendaResponseDTO> pageContent = lista.subList(start, end)
                .stream()
                .map(this::toDTO)
                .toList();

        return new PageImpl<>(pageContent, pageable, lista.size());
    }

    public Agenda salvarReservaComStatusConfirmado(AgendaDTO dto) {
        Usuario criador = usuarioRepository.findById(dto.getCriadorId())
                .orElseThrow(() -> new RuntimeException("Criador não encontrado"));

        Long localId = (dto.getLocalId() == null || dto.getLocalId() == 0) ? 1L : dto.getLocalId();
        Local local = localRepository.findById(localId)
                .orElseThrow(() -> new RuntimeException("Local não encontrado"));

        if (agendaRepository.existsByLocalIdAndDataHoraReuniao(localId, dto.getDataHoraReuniao())) {
            throw new RuntimeException("Já existe um agendamento neste local, data e hora.");
        }

        Agenda agenda = new Agenda();
        agenda.setCriador(criador);
        agenda.setLocal(local);
        agenda.setParticipantes(List.of());
        agenda.setDataHoraReuniao(dto.getDataHoraReuniao());
        agenda.setAssunto(dto.getAssunto());
        agenda.setTipo(TipoAgenda.RESERVA);
        agenda.setStatus(StatusEnum.CONFIRMADO); // <-- fixo como CONFIRMADO

        return agendaRepository.save(agenda);
    }

    public AgendaResponseDTO toDTO(Agenda agenda) {
        AgendaResponseDTO dto = new AgendaResponseDTO();
        dto.setId(agenda.getId());
        dto.setAssunto(agenda.getAssunto());
        dto.setDataHoraReuniao(agenda.getDataHoraReuniao());
        dto.setTipo(agenda.getTipo());

        dto.setStatus(agenda.getStatus());

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

