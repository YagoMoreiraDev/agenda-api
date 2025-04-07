package br.gov.ce.direitoshumanos.api_agenda.models;

import br.gov.ce.direitoshumanos.api_agenda.enums.TipoAgenda;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tb_agenda")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "criador_id")
    private Usuario criador;

    @ManyToMany
    @JoinTable(name = "agenda_participantes",
            joinColumns = @JoinColumn(name = "agenda_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    private List<Usuario> participantes;

    @ManyToOne
    @JoinColumn(name = "local_id")
    private Local local;

    @CreationTimestamp
    private LocalDateTime dataCriacao;

    private LocalDateTime dataHoraReuniao;

    private String assunto;

    @Enumerated(EnumType.STRING)
    private TipoAgenda tipo;
}
