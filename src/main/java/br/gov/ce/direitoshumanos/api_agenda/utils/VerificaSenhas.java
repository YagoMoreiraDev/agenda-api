package br.gov.ce.direitoshumanos.api_agenda.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class VerificaSenhas {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String cpf = "123.456.789-00"; // CPF do ID 128
        String hash = "$2a$10$xiP5ZyvWA4Nxbx28kIIyWunL9dEMxSGTf4nLbkUegCwCxGFWbtvKa"; // senha do ID 128

        boolean corresponde = encoder.matches(cpf, hash);
        System.out.println("CPF confere com hash? " + corresponde);
    }
}
