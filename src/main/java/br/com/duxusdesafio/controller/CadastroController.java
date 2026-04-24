package br.com.duxusdesafio.controller;

import br.com.duxusdesafio.dto.IntegranteDTO;
import br.com.duxusdesafio.dto.TimeDTO;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.service.CadastroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cadastro")
public class CadastroController {

    private final CadastroService cadastroService;

    public CadastroController(CadastroService cadastroService) {
        this.cadastroService = cadastroService;
    }

    @PostMapping("/integrante")
    public ResponseEntity<Integrante> cadastrarIntegrante(@RequestBody @Valid IntegranteDTO dto) {
        Integrante integrante = cadastroService.cadastrarIntegrante(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(integrante);
    }

    @PostMapping("/time")
    public ResponseEntity<Time> cadastrarTime(@RequestBody @Valid TimeDTO dto) {
        Time time = cadastroService.cadastrarTime(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(time);
    }
}
