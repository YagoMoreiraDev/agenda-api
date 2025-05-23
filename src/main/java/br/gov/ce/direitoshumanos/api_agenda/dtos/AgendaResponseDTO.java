package br.gov.ce.direitoshumanos.api_agenda.dtos;

import br.gov.ce.direitoshumanos.api_agenda.enums.StatusEnum;
import br.gov.ce.direitoshumanos.api_agenda.enums.TipoAgenda;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class AgendaResponseDTO {

    private Long id;
    private String assunto;
    private OffsetDateTime dataHoraReuniao;
    private TipoAgenda tipo;
    private StatusEnum status;

    private Long criadorId;
    private String criadorNome;

    private Long localId;
    private String localNome;

    private List<ParticipanteDTO> participantes;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAssunto() { return assunto; }
    public void setAssunto(String assunto) { this.assunto = assunto; }

    public OffsetDateTime getDataHoraReuniao() { return dataHoraReuniao; }
    public void setDataHoraReuniao(OffsetDateTime dataHoraReuniao) {
        // Usa o fuso correto de São Paulo
        this.dataHoraReuniao = dataHoraReuniao;
    }

    public TipoAgenda getTipo() { return tipo; }
    public void setTipo(TipoAgenda tipo) { this.tipo = tipo; }

    public StatusEnum getStatus() { return status; }
    public void setStatus(StatusEnum status) { this.status = status; }

    public Long getCriadorId() { return criadorId; }
    public void setCriadorId(Long criadorId) { this.criadorId = criadorId; }

    public String getCriadorNome() { return criadorNome; }
    public void setCriadorNome(String criadorNome) { this.criadorNome = criadorNome; }

    public Long getLocalId() { return localId; }
    public void setLocalId(Long localId) { this.localId = localId; }

    public String getLocalNome() { return localNome; }
    public void setLocalNome(String localNome) { this.localNome = localNome; }

    public List<ParticipanteDTO> getParticipantes() { return participantes; }
    public void setParticipantes(List<ParticipanteDTO> participantes) { this.participantes = participantes; }
}


