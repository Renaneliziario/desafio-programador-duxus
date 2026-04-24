package br.com.duxusdesafio.controller;

import br.com.duxusdesafio.dto.TimeResponseDTO;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.repository.TimeRepository;
import br.com.duxusdesafio.service.ApiService;
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
public class ApiController {

    private final ApiService apiService;
    private final TimeRepository timeRepository;

    public ApiController(ApiService apiService, TimeRepository timeRepository) {
        this.apiService = apiService;
        this.timeRepository = timeRepository;
    }

    @GetMapping("/time-da-data")
    public TimeResponseDTO getTimeDaData(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        
        List<Time> todosOsTimes = timeRepository.findAll();
        Time time = apiService.timeDaData(data, todosOsTimes);
        
        if (time == null) return null;

        List<String> nomesIntegrantes = time.getComposicaoTime().stream()
                .map(ct -> ct.getIntegrante().getNome())
                .collect(Collectors.toList());

        return new TimeResponseDTO(time.getData(), time.getNomeDoClube(), nomesIntegrantes);
    }

    @GetMapping("/integrante-mais-usado")
    public Integrante getIntegranteMaisUsado(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {
        
        List<Time> todosOsTimes = timeRepository.findAll();
        return apiService.integranteMaisUsado(dataInicial, dataFinal, todosOsTimes);
    }

    @GetMapping("/integrantes-do-time-mais-recorrente")
    public List<String> getIntegrantesDoTimeMaisRecorrente(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {
        
        List<Time> todosOsTimes = timeRepository.findAll();
        return apiService.integrantesDoTimeMaisRecorrente(dataInicial, dataFinal, todosOsTimes);
    }

    @GetMapping("/funcao-mais-recorrente")
    public Map<String, String> getFuncaoMaisRecorrente(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {
        
        List<Time> todosOsTimes = timeRepository.findAll();
        String funcao = apiService.funcaoMaisRecorrente(dataInicial, dataFinal, todosOsTimes);
        return Collections.singletonMap("Função", funcao);
    }

    @GetMapping("/clube-mais-recorrente")
    public Map<String, String> getClubeMaisRecorrente(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {
        
        List<Time> todosOsTimes = timeRepository.findAll();
        String clube = apiService.clubeMaisRecorrente(dataInicial, dataFinal, todosOsTimes);
        return Collections.singletonMap("Clube", clube);
    }

    @GetMapping("/contagem-de-clubes")
    public Map<String, Long> getContagemDeClubesNoPeriodo(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {
        
        List<Time> todosOsTimes = timeRepository.findAll();
        return apiService.contagemDeClubesNoPeriodo(dataInicial, dataFinal, todosOsTimes);
    }

    @GetMapping("/contagem-por-funcao")
    public Map<String, Long> getContagemPorFuncao(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {
        
        List<Time> todosOsTimes = timeRepository.findAll();
        return apiService.contagemPorFuncao(dataInicial, dataFinal, todosOsTimes);
    }
}
