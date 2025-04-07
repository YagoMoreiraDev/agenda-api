package br.gov.ce.direitoshumanos.api_agenda.controllers;

import br.gov.ce.direitoshumanos.api_agenda.dtos.UsuarioDTO;
import br.gov.ce.direitoshumanos.api_agenda.dtos.UsuarioResponseDTO;
import br.gov.ce.direitoshumanos.api_agenda.models.Usuario;
import br.gov.ce.direitoshumanos.api_agenda.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // 🔹 GET - Buscar todos os usuários
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(usuarioService.buscarTodosDTO());
    }

    // 🔹 GET - Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarDTOPorId(id));
    }

    // 🔹 POST - Criar usuário
    @PostMapping
    public ResponseEntity<Usuario> criar(@Valid @RequestBody UsuarioDTO dto) {
        Usuario novo = usuarioService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    // 🔹 PUT - Atualizar usuário
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioDTO dto) {
        Usuario atualizado = usuarioService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    // 🔹 DELETE - Remover usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

