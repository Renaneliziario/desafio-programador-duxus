package br.com.duxusdesafio.service;

import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TesteApiService {

    private final static LocalDate data1993 = LocalDate.of(1993, 1, 1);
    private final static LocalDate data1994 = LocalDate.of(1994, 1, 1);
    private final static LocalDate data1995 = LocalDate.of(1995, 1, 1);

    @Spy
    private ApiService apiService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    // 1. TimeDaData
    @ParameterizedTest
    @MethodSource("testTimeDaDataParams")
    public void testTimeDaData(LocalDate data, List<Time> todosOsTimes, Time esperado) {
        Time timeRetornado = apiService.timeDaData(data, todosOsTimes);
        assertEquals(esperado, timeRetornado);
    }

    // 2. IntegranteMaisUsado
    @ParameterizedTest
    @MethodSource("testIntegranteMaisUsadoParams")
    public void testIntegranteMaisUsado(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes, Integrante esperado) {
        Integrante integranteRetornado = apiService.integranteMaisUsado(dataInicial, dataFinal, todosOsTimes);
        assertEquals(esperado, integranteRetornado);
    }

    // 3. IntegrantesDoTimeMaisRecorrente
    @ParameterizedTest
    @MethodSource("testIntegrantesDoTimeMaisRecorrenteParams")
    public void testIntegrantesDoTimeMaisRecorrente(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes, List<String> esperado) {
        List<String> nomeDosIntegrantesDoTimeMaisRecorrente = apiService.integrantesDoTimeMaisRecorrente(dataInicial, dataFinal, todosOsTimes);

        if (nomeDosIntegrantesDoTimeMaisRecorrente != null) {
            Collections.sort(nomeDosIntegrantesDoTimeMaisRecorrente, Comparator.naturalOrder());
        }
        if (esperado != null) {
            Collections.sort(esperado, Comparator.naturalOrder());
        }

        assertEquals(esperado, nomeDosIntegrantesDoTimeMaisRecorrente);
    }

    // 4. FuncaoMaisRecorrente
    @ParameterizedTest
    @MethodSource("testFuncaoMaisRecorrenteParams")
    public void testFuncaoMaisRecorrente(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes, String esperado) {
        String funcaoMaisRecorrente = apiService.funcaoMaisRecorrente(dataInicial, dataFinal, todosOsTimes);
        assertEquals(esperado, funcaoMaisRecorrente);
    }

    // 5. ClubeMaisRecorrente
    @ParameterizedTest
    @MethodSource("testClubeMaisRecorrenteParams")
    public void testClubeMaisRecorrente(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes, String esperado) {
        String clubeMaisRecorrente = apiService.clubeMaisRecorrente(dataInicial, dataFinal, todosOsTimes);
        assertEquals(esperado, clubeMaisRecorrente);
    }

    // 6. ContagemDeClubesNoPeriodo
    @ParameterizedTest
    @MethodSource("testContagemDeClubesNoPeriodoParams")
    public void testContagemDeClubesNoPeriodo(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes, Map<String, Long> esperado) {
        Map<String, Long> contagemDeClubesNoPeriodo = apiService.contagemDeClubesNoPeriodo(dataInicial, dataFinal, todosOsTimes);
        assertEquals(esperado, contagemDeClubesNoPeriodo);
    }

    // 7. ContagemPorFuncao
    @ParameterizedTest
    @MethodSource("testContagemPorFuncaoParams")
    public void testContagemPorFuncao(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes, Map<String, Long> esperado) {
        Map<String, Long> contagemPorFuncao = apiService.contagemPorFuncao(dataInicial, dataFinal, todosOsTimes);
        assertEquals(esperado, contagemPorFuncao);
    }

    // --- DATA PROVIDERS ---

    static Stream<Arguments> testTimeDaDataParams() {
        DadosParaTesteApiService dadosParaTesteApiService = new DadosParaTesteApiService();
        return Stream.of(
                Arguments.of(data1995, dadosParaTesteApiService.getTodosOsTimes(), dadosParaTesteApiService.getTimeChicagoBullsDe1995()),
                Arguments.of(data1993, dadosParaTesteApiService.getTodosOsTimes(), dadosParaTesteApiService.getTimeDetroidPistonsDe1993())
        );
    }

    static Stream<Arguments> testIntegranteMaisUsadoParams() {
        DadosParaTesteApiService dadosParaTesteApiService = new DadosParaTesteApiService();
        return Stream.of(
                Arguments.of(data1993, data1995, dadosParaTesteApiService.getTodosOsTimes(), dadosParaTesteApiService.getDenis_rodman())
        );
    }

    static Stream<Arguments> testIntegrantesDoTimeMaisRecorrenteParams() {
        DadosParaTesteApiService dadosParaTesteApiService = new DadosParaTesteApiService();
        List<String> integrantesEsperados = Arrays.asList(
                dadosParaTesteApiService.getDenis_rodman().getNome(),
                dadosParaTesteApiService.getMichael_jordan().getNome(),
                dadosParaTesteApiService.getScottie_pippen().getNome()
        );
        return Stream.of(
                Arguments.of(data1993, data1995, dadosParaTesteApiService.getTodosOsTimes(), integrantesEsperados)
        );
    }

    static Stream<Arguments> testFuncaoMaisRecorrenteParams() {
        DadosParaTesteApiService dadosParaTesteApiService = new DadosParaTesteApiService();
        return Stream.of(
                Arguments.of(data1993, data1995, dadosParaTesteApiService.getTodosOsTimes(), "ala")
        );
    }

    static Stream<Arguments> testClubeMaisRecorrenteParams() {
        DadosParaTesteApiService dadosParaTesteApiService = new DadosParaTesteApiService();
        return Stream.of(
                Arguments.of(data1993, data1995, dadosParaTesteApiService.getTodosOsTimes(), dadosParaTesteApiService.getClubeChicagoBulls())
        );
    }

    static Stream<Arguments> testContagemDeClubesNoPeriodoParams() {
        DadosParaTesteApiService dados = new DadosParaTesteApiService();
        Map<String, Long> e1 = new HashMap<>();
        e1.put(dados.getClubeDetroitPistons(), 1L);
        e1.put(dados.getClubeChicagoBulls(), 2L);

        Map<String, Long> e2 = new HashMap<>();
        e2.put(dados.getClubeChicagoBulls(), 2L);

        return Stream.of(
                Arguments.of(data1993, data1995, dados.getTodosOsTimes(), e1),
                Arguments.of(data1994, data1995, dados.getTodosOsTimes(), e2)
        );
    }

    static Stream<Arguments> testContagemPorFuncaoParams() {
        DadosParaTesteApiService dados = new DadosParaTesteApiService();
        Map<String, Long> e = new HashMap<>();
        e.put("ala", 2L);
        e.put("ala-pivô", 1L);
        return Stream.of(
                Arguments.of(data1993, data1995, dados.getTodosOsTimes(), e)
        );
    }
}
