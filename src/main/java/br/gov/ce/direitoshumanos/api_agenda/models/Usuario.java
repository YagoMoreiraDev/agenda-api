package br.gov.ce.direitoshumanos.api_agenda.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_usuario")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true, nullable = false)
    private String cpf;
    private String email;
    private String setor;

    @CreationTimestamp
    private LocalDateTime dataCriacao;

    @OneToMany(mappedBy = "criador")
    @JsonIgnore
    private List<Agenda> agendasCriadas = new ArrayList<>();

    @ManyToMany(mappedBy = "participantes")
    private List<Agenda> agendasParticipadas = new ArrayList<>();
}
