package br.gov.ce.direitoshumanos.api_agenda.dtos;

import br.gov.ce.direitoshumanos.api_agenda.enums.TipoAgenda;

import java.time.LocalDateTime;
import java.util.List;

public class AgendaResponseDTO {

    private Long id;

    private String assunto;
    private LocalDateTime dataHoraReuniao;
    private TipoAgenda tipo;

    private Long criadorId;
    private String criadorNome;

    private Long localId;
    private String localNome;

    private List<ParticipanteDTO> participantes;

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public LocalDateTime getDataHoraReuniao() {
        return dataHoraReuniao;
    }

    public void setDataHoraReuniao(LocalDateTime dataHoraReuniao) {
        this.dataHoraReuniao = dataHoraReuniao;
    }

    public TipoAgenda getTipo() {
        return tipo;
    }

    public void setTipo(TipoAgenda tipo) {
        this.tipo = tipo;
    }

    public Long getCriadorId() {
        return criadorId;
    }

    public void setCriadorId(Long criadorId) {
        this.criadorId = criadorId;
    }

    public String getCriadorNome() {
        return criadorNome;
    }

    public void setCriadorNome(String criadorNome) {
        this.criadorNome = criadorNome;
    }

    public Long getLocalId() {
        return localId;
    }

    public void setLocalId(Long localId) {
        this.localId = localId;
    }

    public String getLocalNome() {
        return localNome;
    }

    public void setLocalNome(String localNome) {
        this.localNome = localNome;
    }

    public List<ParticipanteDTO> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<ParticipanteDTO> participantes) {
        this.participantes = participantes;
    }
}

