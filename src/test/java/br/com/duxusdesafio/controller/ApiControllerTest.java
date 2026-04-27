package br.com.duxusdesafio.controller;

import br.com.duxusdesafio.model.ComposicaoTime;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.repository.TimeRepository;
import br.com.duxusdesafio.service.ApiService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiController.class)
public class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApiService apiService;

    @MockBean
    private TimeRepository timeRepository;

    @Test
    @DisplayName("Deve retornar o time da data no formato JSON esperado pelo desafio")
    public void deveRetornarTimeDaData() throws Exception {
        LocalDate data = LocalDate.of(2021, 1, 15);
        
        Time time = new Time();
        time.setData(data);
        time.setNomeDoClube("Falcons");
        
        Integrante i1 = new Integrante(); i1.setNome("Bangalore");
        Integrante i2 = new Integrante(); i2.setNome("BloodHound");
        
        ComposicaoTime ct1 = new ComposicaoTime(); ct1.setIntegrante(i1);
        ComposicaoTime ct2 = new ComposicaoTime(); ct2.setIntegrante(i2);
        time.setComposicaoTime(Arrays.asList(ct1, ct2));

        when(timeRepository.findAll()).thenReturn(Collections.singletonList(time));
        when(apiService.timeDaData(eq(data), any())).thenReturn(time);

        mockMvc.perform(get("/api/dados/time-da-data")
                .param("data", "2021-01-15")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("2021-01-15"))
                .andExpect(jsonPath("$.clube").value("Falcons"))
                .andExpect(jsonPath("$.integrantes").isArray())
                .andExpect(jsonPath("$.integrantes[0]").value("Bangalore"))
                .andExpect(jsonPath("$.integrantes[1]").value("BloodHound"));
    }

    @Test
    @DisplayName("Deve retornar a função mais recorrente no formato JSON esperado")
    public void deveRetornarFuncaoMaisRecorrente() throws Exception {
        when(apiService.funcaoMaisRecorrente(any(), any(), any())).thenReturn("Meia");

        mockMvc.perform(get("/api/dados/funcao-mais-recorrente")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Função").value("Meia"));
    }

    @Test
    @DisplayName("Deve retornar a contagem de clubes no período")
    public void deveRetornarContagemDeClubes() throws Exception {
        Map<String, Long> contagem = new HashMap<>();
        contagem.put("Falcons", 5L);
        contagem.put("FURIA", 2L);

        when(apiService.contagemDeClubesNoPeriodo(any(), any(), any())).thenReturn(contagem);

        mockMvc.perform(get("/api/dados/contagem-de-clubes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Falcons").value(5))
                .andExpect(jsonPath("$.FURIA").value(2));
    }
}
