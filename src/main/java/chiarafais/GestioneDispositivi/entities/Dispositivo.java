package chiarafais.GestioneDispositivi.entities;

import chiarafais.GestioneDispositivi.enums.StatoDispositivo;
import chiarafais.GestioneDispositivi.enums.TipoDispositivo;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Dispositivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    private TipoDispositivo tipoDispositivo;
    @Enumerated(EnumType.STRING)
    private StatoDispositivo statoDispositivo;
    @ManyToOne
    @JoinColumn(name = "dipendente_id")
    private Dipendente dipendente;

    public Dispositivo(TipoDispositivo tipoDispositivo, StatoDispositivo statoDispositivo) {
        this.tipoDispositivo = tipoDispositivo;
        this.statoDispositivo = statoDispositivo;
    }

}
