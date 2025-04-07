package br.gov.ce.direitoshumanos.api_agenda.dtos;

public class UsuarioResponseDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String setor;

    public UsuarioResponseDTO(Long id, String nome, String cpf, String email, String setor) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.setor = setor;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public String getSetor() {
        return setor;
    }
}
