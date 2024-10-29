package com.example.PrimerParcialMagnetoDDS.controller;

import com.example.PrimerParcialMagnetoDDS.ADNRequest;
import com.example.PrimerParcialMagnetoDDS.service.ADNService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/adn")
public class ADNController {

@Autowired
    private ADNService adnService;


@PostMapping("/mutant")
    public ResponseEntity<Void> isMutant(@RequestBody ADNRequest dnaRequest) {
    String[] dna = dnaRequest.getDna();

    if (adnService.isMutant(dna)) {

        return ResponseEntity.ok().build();
        //Retorna 200 OK si es mutante
    } else {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        //Retorna 403 si no es mutante
    }

}

@GetMapping("/stats")
    public ResponseEntity<Map<String, Object> > obtenerEstadisticas() {
    Map<String, Object> stats = adnService.obtenerEstadisticas();
    return ResponseEntity.ok(stats);
}

}
