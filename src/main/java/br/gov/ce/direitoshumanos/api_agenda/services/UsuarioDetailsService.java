package br.gov.ce.direitoshumanos.api_agenda.services;

import br.gov.ce.direitoshumanos.api_agenda.models.Usuario;
import br.gov.ce.direitoshumanos.api_agenda.models.UsuarioDetails;
import br.gov.ce.direitoshumanos.api_agenda.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCpf(cpf)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com CPF: " + cpf));
        return new UsuarioDetails(usuario);
    }
}
