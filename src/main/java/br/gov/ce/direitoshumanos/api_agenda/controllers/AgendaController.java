package br.gov.ce.direitoshumanos.api_agenda.controllers;

import br.gov.ce.direitoshumanos.api_agenda.dtos.AgendaDTO;
import br.gov.ce.direitoshumanos.api_agenda.dtos.AgendaResponseDTO;
import br.gov.ce.direitoshumanos.api_agenda.enums.StatusEnum;
import br.gov.ce.direitoshumanos.api_agenda.models.Agenda;
import br.gov.ce.direitoshumanos.api_agenda.services.AgendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agendas")
@RequiredArgsConstructor
public class AgendaController {

    private final AgendaService agendaService;

    // 🔹 GET - Reuniões futuras criadas pelo usuário
    @GetMapping("/futuras/criador")
    public ResponseEntity<Page<AgendaResponseDTO>> listarReunioesFuturasCriadas(
            @RequestParam Long usuarioId,
            @RequestParam(defaultValue = "0") int pagina
    ) {
        Page<AgendaResponseDTO> dtos = agendaService.listarReunioesFuturasCriadasPorDTO(usuarioId, pagina);
        return ResponseEntity.ok(dtos);
    }

    // 🔹 GET - Reuniões futuras onde o usuário participa
    @GetMapping("/futuras/participante")
    public ResponseEntity<Page<AgendaResponseDTO>> listarReunioesFuturasComoParticipante(
            @RequestParam Long usuarioId,
            @RequestParam(defaultValue = "0") int pagina
    ) {
        Page<AgendaResponseDTO> dtos = agendaService.listarReunioesFuturasOndeParticipaDTO(usuarioId, pagina);
        return ResponseEntity.ok(dtos);
    }

    // 🔹 GET - Histórico de reuniões passadas (criador e participante)
    @GetMapping("/historico")
    public ResponseEntity<Page<AgendaResponseDTO>> listarHistoricoDeReunioes(
            @RequestParam Long usuarioId,
            @RequestParam(defaultValue = "0") int pagina
    ) {
        Page<AgendaResponseDTO> dtos = agendaService.listarHistoricoDeReunioesDoUsuarioDTO(usuarioId, pagina);
        return ResponseEntity.ok(dtos);
    }

    // 🔹 GET - Buscar uma agenda específica
    @GetMapping("/{id}")
    public ResponseEntity<AgendaResponseDTO> buscarPorId(@PathVariable Long id) {
        Agenda agenda = agendaService.buscarPorId(id);
        AgendaResponseDTO dto = agendaService.toDTO(agenda);
        return ResponseEntity.ok(dto);
    }

    // 🔹 POST - Criar nova agenda
    @PostMapping
    public ResponseEntity<AgendaResponseDTO> criar(@Valid @RequestBody AgendaDTO dto) {
        Agenda nova = agendaService.salvar(dto);
        AgendaResponseDTO response = agendaService.toDTO(nova);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 🔹 PUT - Atualizar agenda
    @PutMapping("/{id}")
    public ResponseEntity<AgendaResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody AgendaDTO dto) {
        Agenda atualizada = agendaService.atualizar(id, dto);
        return ResponseEntity.ok(agendaService.toDTO(atualizada));
    }

    // 🔹 DELETE - Remover agenda
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAgenda(@PathVariable Long id) {
        agendaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // 🔹 GET - Buscar agendas por status
    @GetMapping("/status")
    public Page<AgendaResponseDTO> listarPorStatus(
            @RequestParam StatusEnum status,
            @RequestParam Long usuarioId,
            @RequestParam(defaultValue = "0") int pagina
    ) {
        return agendaService.listarPorStatus(status, usuarioId, pagina);
    }

    // 🔹 GET - Buscar todas as agendas por status CONFIRMADO
    @GetMapping("/confirmadas")
    public ResponseEntity<List<AgendaResponseDTO>> listarConfirmadas(@RequestParam Long usuarioId) {
        List<AgendaResponseDTO> dtos = agendaService.listarReunioesConfirmadasDoUsuario(usuarioId);
        return ResponseEntity.ok(dtos);
    }

    // 🔹 GET - Buscar todas as agendas por status CONFIRMADO paginada
    @GetMapping("/confirmadas-futuras")
    public ResponseEntity<Page<AgendaResponseDTO>> listarConfirmadasFuturas(
            @RequestParam Long usuarioId,
            @RequestParam(defaultValue = "0") int pagina
    ) {
        return ResponseEntity.ok(agendaService.listarConfirmadasFuturas(usuarioId, pagina));
    }

    // 🔹 PUT - Confirmar uma agenda
    @PutMapping("/{id}/confirmar")
    public ResponseEntity<AgendaResponseDTO> confirmarAgenda(@PathVariable Long id) {
        Agenda confirmada = agendaService.atualizarStatus(id, StatusEnum.CONFIRMADO);
        return ResponseEntity.ok(agendaService.toDTO(confirmada));
    }

    // 🔹 PUT - Cancelar uma agenda
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<AgendaResponseDTO> cancelarAgenda(@PathVariable Long id) {
        Agenda cancelada = agendaService.atualizarStatus(id, StatusEnum.CANCELADO);
        return ResponseEntity.ok(agendaService.toDTO(cancelada));
    }
}
