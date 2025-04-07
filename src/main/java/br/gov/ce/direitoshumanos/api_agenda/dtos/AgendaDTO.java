package br.gov.ce.direitoshumanos.api_agenda.dtos;

import br.gov.ce.direitoshumanos.api_agenda.enums.TipoAgenda;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendaDTO {

    @NotNull(message = "O ID do criador é obrigatório")
    private Long criadorId;

    private List<Long> participantesIds = new ArrayList<>();

    @NotNull(message = "O ID do local é obrigatório")
    private Long localId;

    @NotNull(message = "A data e hora da reunião é obrigatória")
    private LocalDateTime dataHoraReuniao;

    @NotBlank(message = "O assunto da reunião é obrigatório")
    private String assunto;

    @NotNull(message = "O tipo da agenda é obrigatório")
    private TipoAgenda tipo;
}

