package br.gov.ce.direitoshumanos.api_agenda.dtos;

public class LocalResponseDTO {

    private Long id;
    private String nome;

    public LocalResponseDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
