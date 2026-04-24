package br.com.duxusdesafio.controller;

import br.com.duxusdesafio.dto.IntegranteResponseDTO;
import br.com.duxusdesafio.dto.TimeResponseDTO;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.repository.TimeRepository;
import br.com.duxusdesafio.service.ApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dados")
@Tag(name = "Processamento", description = "Endpoints para consulta de estatísticas e dados processados")
public class ApiController {

    private final ApiService apiService;
    private final TimeRepository timeRepository;

    public ApiController(ApiService apiService, TimeRepository timeRepository) {
        this.apiService = apiService;
        this.timeRepository = timeRepository;
    }

    @GetMapping("/time-da-data")
    @Operation(summary = "Time da Data", description = "Retorna o time escalado em uma data específica")
    public TimeResponseDTO getTimeDaData(
            @RequestParam(name = "data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        
        List<Time> todosOsTimes = timeRepository.findAll();
        Time time = apiService.timeDaData(data, todosOsTimes);
        
        if (time == null) return null;

        List<String> nomesIntegrantes = time.getComposicaoTime().stream()
                .map(ct -> ct.getIntegrante().getNome())
                .collect(Collectors.toList());

        return new TimeResponseDTO(time.getData(), time.getNomeDoClube(), nomesIntegrantes);
    }

    @GetMapping("/integrante-mais-usado")
    @Operation(summary = "Integrante Mais Usado", description = "Identifica o integrante que apareceu no maior número de times no período")
    public IntegranteResponseDTO getIntegranteMaisUsado(
            @RequestParam(name = "dataInicial", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(name = "dataFinal", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {
        
        List<Time> todosOsTimes = timeRepository.findAll();
        Integrante integrante = apiService.integranteMaisUsado(dataInicial, dataFinal, todosOsTimes);
        
        if (integrante == null) return null;
        
        return new IntegranteResponseDTO(integrante.getId(), integrante.getNome(), integrante.getFuncao());
    }

    @GetMapping("/integrantes-do-time-mais-recorrente")
    @Operation(summary = "Time Mais Recorrente", description = "Retorna os nomes dos integrantes do time que mais se repetiu no período")
    public List<String> getIntegrantesDoTimeMaisRecorrente(
            @RequestParam(name = "dataInicial", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(name = "dataFinal", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {
        
        List<Time> todosOsTimes = timeRepository.findAll();
        return apiService.integrantesDoTimeMaisRecorrente(dataInicial, dataFinal, todosOsTimes);
    }

    @GetMapping("/funcao-mais-recorrente")
    @Operation(summary = "Função Mais Recorrente", description = "Identifica a função com maior número de integrantes únicos no período")
    public Map<String, String> getFuncaoMaisRecorrente(
            @RequestParam(name = "dataInicial", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(name = "dataFinal", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {
        
        List<Time> todosOsTimes = timeRepository.findAll();
        String funcao = apiService.funcaoMaisRecorrente(dataInicial, dataFinal, todosOsTimes);
        return Collections.singletonMap("Função", funcao);
    }

    @GetMapping("/clube-mais-recorrente")
    @Operation(summary = "Clube Mais Recorrente", description = "Identifica o nome do clube mais comum no período")
    public Map<String, String> getClubeMaisRecorrente(
            @RequestParam(name = "dataInicial", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(name = "dataFinal", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {
        
        List<Time> todosOsTimes = timeRepository.findAll();
        String clube = apiService.clubeMaisRecorrente(dataInicial, dataFinal, todosOsTimes);
        return Collections.singletonMap("Clube", clube);
    }

    @GetMapping("/contagem-de-clubes")
    @Operation(summary = "Contagem de Clubes", description = "Retorna o número de aparições de cada clube no período")
    public Map<String, Long> getContagemDeClubesNoPeriodo(
            @RequestParam(name = "dataInicial", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(name = "dataFinal", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {
        
        List<Time> todosOsTimes = timeRepository.findAll();
        return apiService.contagemDeClubesNoPeriodo(dataInicial, dataFinal, todosOsTimes);
    }

    @GetMapping("/contagem-por-funcao")
    @Operation(summary = "Contagem por Função", description = "Retorna o número de integrantes únicos em cada função no período")
    public Map<String, Long> getContagemPorFuncao(
            @RequestParam(name = "dataInicial", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(name = "dataFinal", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {
        
        List<Time> todosOsTimes = timeRepository.findAll();
        return apiService.contagemPorFuncao(dataInicial, dataFinal, todosOsTimes);
    }
}
