package chiarafais.GestioneDispositivi.services;

import chiarafais.GestioneDispositivi.entities.Dipendente;
import chiarafais.GestioneDispositivi.exceptions.BadRequestException;
import chiarafais.GestioneDispositivi.exceptions.NotFoundException;
import chiarafais.GestioneDispositivi.payloads.NewDipendenteDTO;
import chiarafais.GestioneDispositivi.repositories.DipendentiDAO;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class DipendentiService {
    @Autowired
    private DipendentiDAO dipendentiDAO;

    @Autowired
    private Cloudinary cloudinaryUploader;

    public DipendentiService(DipendentiDAO dipendentiDAO) {
        this.dipendentiDAO = dipendentiDAO;
    }

    public List<Dipendente> getDipendentiList() {
        return dipendentiDAO.findAll();
    }

    public Dipendente saveDipendente(NewDipendenteDTO newDipendenteDTO) {

        if (dipendentiDAO.existsByEmail(newDipendenteDTO.email())) {
            throw new BadRequestException("L'email " + newDipendenteDTO.email() + " è già in uso, quindi il dipendente risulta già registrato!");
        }

        Dipendente dipendente = new Dipendente(newDipendenteDTO.username(),newDipendenteDTO.name(), newDipendenteDTO.surname(), newDipendenteDTO.email());
        System.out.println(dipendente);
        dipendente.setPropic("avatar");
        return dipendentiDAO.save(dipendente);
    }

    public Dipendente findById(int id) {
        return dipendentiDAO.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public Dipendente findByIdAndUpdate(int id, Dipendente updatedDipendente) {
        Dipendente found = findById(id);
        found.setName(updatedDipendente.getName());
        found.setSurname(updatedDipendente.getSurname());
        return dipendentiDAO.save(found);
    }

    public void findByIdAndDelete(int dipendenteId) {
        dipendentiDAO.deleteById(dipendenteId);
    }

    public String uploadImage(MultipartFile image) throws IOException {
        String url = (String) cloudinaryUploader.uploader().upload(image.getBytes(), ObjectUtils.emptyMap()).get("url");
        return url;
    }

    public Dipendente uploadDipendenteImage (MultipartFile image,int dipendenteId) throws IOException {
        Dipendente found = this.findById(dipendenteId);
        found.setPropic(this.uploadImage(image));
        this.dipendentiDAO.save(found);
        return found;
    }

    public Page<Dipendente> getDipendenti(int page, int size, String sortBy){
        if(size > 70) size = 70;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.dipendentiDAO.findAll(pageable);
    }

}
