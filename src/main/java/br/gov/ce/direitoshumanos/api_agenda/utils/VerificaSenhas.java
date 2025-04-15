package br.gov.ce.direitoshumanos.api_agenda.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class VerificaSenhas {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String cpf = "055.548.143-36"; // CPF do ID 128
        String hash = "$2a$10$S90adlsEZZf1YG0eg.TpUunpKMmuF9Q69/4tpNthaOi99apwCLjSS"; // senha do ID 128

        boolean corresponde = encoder.matches(cpf, hash);
        System.out.println("CPF confere com hash? " + corresponde);
    }
}
