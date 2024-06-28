package chiarafais.GestioneDispositivi.controllers;

import chiarafais.GestioneDispositivi.entities.Dispositivo;
import chiarafais.GestioneDispositivi.exceptions.BadRequestException;
import chiarafais.GestioneDispositivi.payloads.NewDispositivoDTO;
import chiarafais.GestioneDispositivi.payloads.NewDispositivoRespDTO;
import chiarafais.GestioneDispositivi.services.DispositiviService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dispositivi")
public class DispositiviController {
    @Autowired
    private DispositiviService dispositiviService;

    //    1. POST http://localhost:3001/dispositivi (+ body)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewDispositivoRespDTO saveDispositivo(@RequestBody @Validated NewDispositivoDTO body, BindingResult validation){

        if(validation.hasErrors()) {
            System.out.println(validation.getAllErrors());
            throw new BadRequestException(validation.getAllErrors());
        }
//        System.out.println(body);
        return new NewDispositivoRespDTO(this.dispositiviService.saveDispositivo(body).getId());}

    //      1.2 POST per assegnare uno o più dispositivi al dipendente
//    http://localhost:3001/dispositivi/assegna/{dipendenteId} + body[dispositivoId, dispositivoId ...]
    @PostMapping("/assegna/{dipendenteId}")
    public ResponseEntity<String> assegnaDispositivi(@PathVariable int dipendenteId, @RequestBody List<Integer> dispositiviId) {
        dispositiviService.assegnaDispositivi(dipendenteId, dispositiviId);
        return ResponseEntity.ok("Dispositivi assegnati con successo al dipendente con ID: " + dipendenteId);
    }


    // 2. GET http://localhost:3001/dispositivi/{dispositivoId}
    @GetMapping("/{dispositivoId}")
    private Dispositivo findDispositivoById(@PathVariable int dispositivoId){
        return this.dispositiviService.findById(dispositivoId);
    }

    //    3. GET http://localhost:3001/dispositivi
    @GetMapping
    private List<Dispositivo> getAllDispositivo(){
        return this.dispositiviService.getDispositiviList();
    }

    //    3.1 Paginazione e ordinamento http://localhost:3001/dispositivi/page
    @GetMapping("/page")
    public Page<Dispositivo> getAllDispositivi(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "1") int size,
                                               @RequestParam(defaultValue = "id") String sortBy) {
        return this.dispositiviService.getDispositivi(page, size, sortBy);
    }

    // 4. PUT http://localhost:3001/dispositivi/{dispositivoId} (+ body)
    @PutMapping("/{dispositivoId}")
    private Dispositivo findByIdAndUpdate(@PathVariable int dispositivoId, @RequestBody Dispositivo body){
        return this.dispositiviService.findByIdAndUpdate(dispositivoId, body);
    }



    // 5. DELETE http://localhost:3001/dispositivi/{dispositivoId}
    @DeleteMapping("/{dispositivoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDispositivoById(@PathVariable int dispositivoId) {
        this.dispositiviService.findByIdAndDelete(dispositivoId);
    }
}