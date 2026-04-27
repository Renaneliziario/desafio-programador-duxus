package br.com.duxusdesafio.service;

import br.com.duxusdesafio.model.Time;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ApiServiceExperimentoTest {

    private final ApiService apiService = new ApiService();
    private final DadosParaTesteApiService dados = new DadosParaTesteApiService();

    @Test
    @DisplayName("Deve lidar corretamente com datas nulas (processar todo o período)")
    public void deveLidarComDatasNulas() {
        List<Time> todos = dados.getTodosOsTimes();
        
        // Se as datas são nulas, deve retornar o integrante mais usado de toda a história
        assertNotNull(apiService.integranteMaisUsado(null, null, todos));
        
        // Deve retornar a contagem de todos os clubes sem filtrar data
        Map<String, Long> contagem = apiService.contagemDeClubesNoPeriodo(null, null, todos);
        assertEquals(2, contagem.size()); // Bulls e Pistons
    }

    @Test
    @DisplayName("Deve retornar vazio ou null ao processar uma lista de times vazia")
    public void deveLidarComListaVazia() {
        List<Time> listaVazia = Collections.emptyList();
        LocalDate data = LocalDate.now();

        assertNull(apiService.timeDaData(data, listaVazia));
        assertNull(apiService.integranteMaisUsado(data, data, listaVazia));
        assertTrue(apiService.integrantesDoTimeMaisRecorrente(data, data, listaVazia).isEmpty());
        assertNull(apiService.funcaoMaisRecorrente(data, data, listaVazia));
        assertTrue(apiService.contagemDeClubesNoPeriodo(data, data, listaVazia).isEmpty());
    }

    @Test
    @DisplayName("Deve respeitar os limites inclusivos das datas")
    public void deveRespeitarLimitesInclusivos() {
        List<Time> todos = dados.getTodosOsTimes();
        LocalDate dataExata93 = LocalDate.of(1993, 1, 1);
        
        // Filtro com data inicial e final iguais (apenas o dia 01/01/1993)
        Map<String, Long> contagem = apiService.contagemDeClubesNoPeriodo(dataExata93, dataExata93, todos);
        
        assertEquals(1, contagem.size());
        assertTrue(contagem.containsKey("Detroit Pistons"));
    }

    @Test
    @DisplayName("Integridade de Dados: Validar se os IDs dos integrantes foram atribuídos corretamente")
    public void validarIntegridadeDaMassaDeDadosOriginal() {
        // Michael Jordan deve ser ID 1
        assertEquals(1L, dados.getMichael_jordan().getId(), "ID do Michael Jordan incorreto");

        // Denis Rodman deve ser ID 2
        assertEquals(2L, dados.getDenis_rodman().getId(), "ID do Denis Rodman está incorreto");

        // Scottie Pippen deve ser ID 3
        assertNotNull(dados.getScottie_pippen().getId(), "Scottie Pippen está com ID nulo");
        assertEquals(3L, dados.getScottie_pippen().getId(), "ID do Scottie Pippen incorreto");

        // Garantir que Rodman e Pippen não dividem o mesmo ID
        assertNotEquals(dados.getDenis_rodman().getId(), dados.getScottie_pippen().getId(), 
            "Inconsistência detectada: Rodman e Pippen possuem o mesmo ID na massa de dados original.");
    }
}
