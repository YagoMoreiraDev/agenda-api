package br.gov.ce.direitoshumanos.api_agenda.dtos;

public record LoginResponse(
        String token,
        Long id,                 // <-- isso aqui!
        String nome,
        String cpf,
        boolean precisaAlterarSenha
) {}
