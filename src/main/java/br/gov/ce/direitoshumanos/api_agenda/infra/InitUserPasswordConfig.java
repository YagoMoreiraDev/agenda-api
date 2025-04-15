package br.gov.ce.direitoshumanos.api_agenda.infra;

import br.gov.ce.direitoshumanos.api_agenda.repositories.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class InitUserPasswordConfig {
    @Bean
    CommandLineRunner initUsersWithCpfAsPassword(UsuarioRepository usuarioRepository, PasswordEncoder encoder) {
        return args -> {
            usuarioRepository.findAll().forEach(usuario -> {
                if (usuario.getSenha() == null) {
                    String senhaCriptografada = encoder.encode(usuario.getCpf());
                    usuario.setSenha(senhaCriptografada);
                    usuario.setPrecisaAlterarSenha(true);
                    usuarioRepository.save(usuario);
                }
            });
        };
    }
}
