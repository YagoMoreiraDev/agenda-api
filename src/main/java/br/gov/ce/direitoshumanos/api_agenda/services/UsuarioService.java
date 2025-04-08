package br.gov.ce.direitoshumanos.api_agenda.services;

import br.gov.ce.direitoshumanos.api_agenda.dtos.UsuarioDTO;
import br.gov.ce.direitoshumanos.api_agenda.dtos.UsuarioResponseDTO;
import br.gov.ce.direitoshumanos.api_agenda.models.Usuario;
import br.gov.ce.direitoshumanos.api_agenda.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));
    }

    public Usuario salvar(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setCpf(dto.getCpf());
        usuario.setEmail(dto.getEmail());
        usuario.setSetor(dto.getSetor());

        return usuarioRepository.save(usuario);
    }

    public Usuario atualizar(Long id, UsuarioDTO dto) {
        Usuario existente = buscarPorId(id);
        existente.setNome(dto.getNome());
        existente.setCpf(dto.getCpf());
        existente.setEmail(dto.getEmail());
        existente.setSetor(dto.getSetor());

        return usuarioRepository.save(existente);
    }

    public void deletar(Long id) {
        Usuario existente = buscarPorId(id);
        usuarioRepository.delete(existente);
    }

    public List<UsuarioResponseDTO> buscarTodosDTO() {
        return usuarioRepository.findAll().stream()
                .map(u -> new UsuarioResponseDTO(
                        u.getId(),
                        u.getNome(),
                        u.getCpf(),
                        u.getEmail(),
                        u.getSetor()
                ))
                .toList();
    }

    public UsuarioResponseDTO buscarDTOPorId(Long id) {
        Usuario u = buscarPorId(id);
        return new UsuarioResponseDTO(u.getId(), u.getNome(), u.getCpf(), u.getEmail(), u.getSetor());
    }

    public List<UsuarioResponseDTO> filtrarPorNome(String nome) {
        return usuarioRepository.findByNomeStartingWithIgnoreCase(nome).stream()
                .map(u -> new UsuarioResponseDTO(
                        u.getId(),
                        u.getNome(),
                        u.getCpf(),
                        u.getEmail(),
                        u.getSetor()
                )).toList();
    }

    public List<UsuarioResponseDTO> buscarPorNome(String nome) {
        return usuarioRepository.findByNomeStartingWithIgnoreCase(nome)
                .stream()
                .map(u -> new UsuarioResponseDTO(
                        u.getId(), u.getNome(), u.getCpf(), u.getEmail(), u.getSetor()))
                .toList();
    }
}

