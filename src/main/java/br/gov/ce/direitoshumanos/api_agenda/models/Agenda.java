package br.gov.ce.direitoshumanos.api_agenda.models;

import br.gov.ce.direitoshumanos.api_agenda.enums.StatusEnum;
import br.gov.ce.direitoshumanos.api_agenda.enums.TipoAgenda;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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

    @Column(name = "data_hora_reuniao", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime dataHoraReuniao;

    private String assunto;

    @Enumerated(EnumType.STRING)
    private TipoAgenda tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusEnum status = StatusEnum.PENDENTE;
}
