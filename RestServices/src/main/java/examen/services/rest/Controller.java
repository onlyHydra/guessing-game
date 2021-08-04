package examen.services.rest;


import domain.Joc;
import domain.Runda;
import examen.persistence.JocRepository;
import examen.persistence.RundaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.StreamSupport;

@CrossOrigin
@RestController
@RequestMapping("/examen")
public class Controller {
    @Autowired
    private JocRepository jocRepository;
    @Autowired
    private RundaRepository rundaRepository;

    @RequestMapping(value = "/cuvinte-propuse/joc/{id}", method = RequestMethod.GET)
    public String[] findCuvinte(@PathVariable Integer id) {
        int size = (int) StreamSupport.stream(jocRepository.findByJocId(id).spliterator(), false).count();
        System.out.println(size);
        Joc[] result = new Joc[size];
        result = ((List<Joc>) jocRepository.findByJocId(id)).toArray(result);
        String[] resultF = new String[size];
        for(int i=0;i<size;i++){
            resultF[i]=result[i].getUsername()+ " a propus "+ result[i].getCuvantPropus();
        }
        return resultF;
    }

    @RequestMapping(value = "/propuneri-punctaj/jucator/{id}/joc/{idJoc}", method = RequestMethod.GET)
    public String[] findLitereSiPunctaje(@PathVariable String id, @PathVariable Integer idJoc) {
        int size = (int) StreamSupport.stream(rundaRepository.findByGameIdAndUser(idJoc,id).spliterator(), false).count();
        Runda[] result = new Runda[size];
        result = ((List<Runda>) rundaRepository.findByGameIdAndUser(idJoc,id)).toArray(result);
        String[] resultF = new String[size];
        for(int i=0;i<size;i++){
            resultF[i]="runda "+result[i].getNrRunda()+ ": a propus: "+ result[i].getLiteraAleasa()+ " si a castigat: "+ result[i].getNrPuncteCastigate();
        }
        return resultF;
    }



    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String Error(Exception e) {
        return e.getMessage();
    }
}
