package chiarafais.GestioneDispositivi.services;

import chiarafais.GestioneDispositivi.entities.Dipendente;
import chiarafais.GestioneDispositivi.entities.Dispositivo;
import chiarafais.GestioneDispositivi.enums.StatoDispositivo;
import chiarafais.GestioneDispositivi.enums.TipoDispositivo;
import chiarafais.GestioneDispositivi.exceptions.BadRequestException;
import chiarafais.GestioneDispositivi.exceptions.NotFoundException;
import chiarafais.GestioneDispositivi.payloads.NewDispositivoDTO;
import chiarafais.GestioneDispositivi.repositories.DipendentiDAO;
import chiarafais.GestioneDispositivi.repositories.DispositiviDAO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DispositiviService {
    @Autowired
    private DispositiviDAO dispositiviDAO;
    @Autowired
    private DipendentiDAO dipendentiDAO;

    public DispositiviService(DispositiviDAO dispositiviDAO) {
        this.dispositiviDAO = dispositiviDAO;
    }

    public List<Dispositivo> getDispositiviList() {
        return dispositiviDAO.findAll();
    }

    public static TipoDispositivo convertToStringTipo(NewDispositivoDTO newDispositivoDTO){
        try {
            return TipoDispositivo.valueOf(newDispositivoDTO.TipoDispositivo().toUpperCase());
        }catch (IllegalArgumentException exception){
            throw new BadRequestException("Il tipo del dispositivo deve essere SMARTPHONE,TABLET O LAPTOP!");
        }
    }

    public static StatoDispositivo convertToStringStato(NewDispositivoDTO newDispositivoDTO){
        try {
            return StatoDispositivo.valueOf(newDispositivoDTO.StatoDispositivo().toUpperCase());
        }catch (IllegalArgumentException exception){
            throw new BadRequestException("lo stato del dispositivo deve essere DISPONIBILE,ASSEGNATO,IN_MANUTENZIONE o DISMESSO!");
        }
    }

    public Dispositivo saveDispositivo(NewDispositivoDTO newDispositivoDTO) {

        Dispositivo dispositivo = new Dispositivo(convertToStringTipo(newDispositivoDTO),convertToStringStato(newDispositivoDTO));
//        System.out.println(dispositivo);
        return dispositiviDAO.save(dispositivo);
    }

    public Dispositivo findById(int id) {
        return dispositiviDAO.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public Dispositivo findByIdAndUpdate(int id, Dispositivo updatedDispositivo) {
        Dispositivo found = findById(id);
        found.setTipoDispositivo(updatedDispositivo.getTipoDispositivo());
        found.setStatoDispositivo(updatedDispositivo.getStatoDispositivo());
        return dispositiviDAO.save(found);
    }

    public void findByIdAndDelete(int dispositivoId) {
        dispositiviDAO.deleteById(dispositivoId);
    }

//    @Transactional
//    public Dispositivo assegnaDispositivo(int dispositivoId, int dipendenteId) {
//        Dispositivo dispositivo = dispositiviDAO.findById(dispositivoId)
//                .orElseThrow(() -> new EntityNotFoundException("Dispositivo non trovato con ID: " + dispositivoId));
//        Dipendente dipendente = dipendentiDAO.findById(dipendenteId)
//                .orElseThrow(() -> new EntityNotFoundException("Dipendente non trovato con ID: " + dipendenteId));
//        dispositivo.setDipendente(dipendente);
//        return dispositiviDAO.save(dispositivo);
//    }

    @Transactional
    public Dipendente assegnaDispositivi(int dipendenteId, int dispositiviId) {
        Dipendente dipendente = dipendentiDAO.findById(dipendenteId)
                .orElseThrow(() -> new EntityNotFoundException("Dipendente non trovato con ID: " + dipendenteId));
        Dispositivo dispositivi = dispositiviDAO.findById(dispositiviId)
        .orElseThrow(() -> new EntityNotFoundException("Dipendente non trovato con ID: " + dispositiviId));
        List<Dispositivo> prova= new ArrayList<>();
        prova.add(dispositivi);
        dipendente.setDispositivi(prova);
        return dipendentiDAO.save(dipendente);
    }

    public Page<Dispositivo> getDispositivi(int page, int size, String sortBy){
        if(size > 50) size = 50;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.dispositiviDAO.findAll(pageable);
    }

}
