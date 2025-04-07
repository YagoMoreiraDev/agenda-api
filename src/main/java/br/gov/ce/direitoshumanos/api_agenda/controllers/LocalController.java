package br.gov.ce.direitoshumanos.api_agenda.controllers;

import br.gov.ce.direitoshumanos.api_agenda.dtos.LocalDTO;
import br.gov.ce.direitoshumanos.api_agenda.dtos.LocalResponseDTO;
import br.gov.ce.direitoshumanos.api_agenda.models.Local;
import br.gov.ce.direitoshumanos.api_agenda.services.LocalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locais")
@RequiredArgsConstructor
public class LocalController {

    private final LocalService localService;

    // 🔹 GET - Buscar todos os locais
    @GetMapping
    public ResponseEntity<List<LocalResponseDTO>> listarTodos() {
        return ResponseEntity.ok(localService.buscarTodosDTO());
    }

    // 🔹 GET - Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<LocalResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(localService.buscarDTOPorId(id));
    }

    // 🔹 POST - Criar novo local
    @PostMapping
    public ResponseEntity<Local> criar(@Valid @RequestBody LocalDTO dto) {
        Local novo = localService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    // 🔹 PUT - Atualizar local
    @PutMapping("/{id}")
    public ResponseEntity<Local> atualizar(@PathVariable Long id, @Valid @RequestBody LocalDTO dto) {
        Local atualizado = localService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    // 🔹 DELETE - Remover local
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        localService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

