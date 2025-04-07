package br.gov.ce.direitoshumanos.api_agenda.services;

import br.gov.ce.direitoshumanos.api_agenda.dtos.LocalDTO;
import br.gov.ce.direitoshumanos.api_agenda.dtos.LocalResponseDTO;
import br.gov.ce.direitoshumanos.api_agenda.models.Local;
import br.gov.ce.direitoshumanos.api_agenda.repositories.LocalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocalService {

    private final LocalRepository localRepository;

    public List<Local> buscarTodos() {
        return localRepository.findAll();
    }

    public Local buscarPorId(Long id) {
        return localRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Local não encontrado com ID: " + id));
    }

    public Local salvar(LocalDTO dto) {
        Local local = new Local();
        local.setNome(dto.getNome());

        return localRepository.save(local);
    }

    public Local atualizar(Long id, LocalDTO dto) {
        Local existente = buscarPorId(id);
        existente.setNome(dto.getNome());

        return localRepository.save(existente);
    }

    public void deletar(Long id) {
        Local existente = buscarPorId(id);
        localRepository.delete(existente);
    }

    public List<LocalResponseDTO> buscarTodosDTO() {
        return localRepository.findAll().stream()
                .map(l -> new LocalResponseDTO(l.getId(), l.getNome()))
                .toList();
    }

    public LocalResponseDTO buscarDTOPorId(Long id) {
        Local l = buscarPorId(id);
        return new LocalResponseDTO(l.getId(), l.getNome());
    }
}

