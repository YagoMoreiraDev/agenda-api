package br.gov.ce.direitoshumanos.api_agenda.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_local")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Local {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @CreationTimestamp
    private LocalDateTime dataDeCriacao;

    @OneToMany(mappedBy = "local")
    private List<Agenda> agendas = new ArrayList<>();
}
