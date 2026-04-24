package br.com.duxusdesafio.controller;

import br.com.duxusdesafio.dto.TimeResponseDTO;
import br.com.duxusdesafio.dto.IntegranteDTO;
import br.com.duxusdesafio.dto.TimeDTO;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.service.CadastroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cadastro")
@Tag(name = "Cadastro", description = "Endpoints para inserção de novos dados no sistema")
public class CadastroController {

    private final CadastroService cadastroService;

    public CadastroController(CadastroService cadastroService) {
        this.cadastroService = cadastroService;
    }

    @PostMapping("/integrante")
    @Operation(summary = "Cadastra um novo integrante", description = "Cria um jogador com nome e função")
    public ResponseEntity<Integrante> cadastrarIntegrante(@RequestBody @Valid IntegranteDTO dto) {
        Integrante integrante = cadastroService.cadastrarIntegrante(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(integrante);
    }

    @PostMapping("/time")
    @Operation(summary = "Escala um novo time", description = "Cria um time vinculado a uma lista de integrantes existentes")
    public ResponseEntity<TimeResponseDTO> cadastrarTime(@RequestBody @Valid TimeDTO dto) {
        Time time = cadastroService.cadastrarTime(dto);

        List<String> nomesIntegrantes = time.getComposicaoTime().stream()
                .map(ct -> ct.getIntegrante().getNome())
                .collect(Collectors.toList());

        TimeResponseDTO response = new TimeResponseDTO(
                time.getData(),
                time.getNomeDoClube(),
                nomesIntegrantes
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
