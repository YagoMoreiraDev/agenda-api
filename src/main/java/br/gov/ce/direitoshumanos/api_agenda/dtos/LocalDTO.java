package br.gov.ce.direitoshumanos.api_agenda.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocalDTO {

    private Long id;

    @NotBlank(message = "O nome do local é obrigatório")
    private String nome;
}
