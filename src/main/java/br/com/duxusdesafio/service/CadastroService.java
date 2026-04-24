package br.com.duxusdesafio.service;

import br.com.duxusdesafio.dto.IntegranteDTO;
import br.com.duxusdesafio.dto.TimeDTO;
import br.com.duxusdesafio.model.ComposicaoTime;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.repository.ComposicaoTimeRepository;
import br.com.duxusdesafio.repository.IntegranteRepository;
import br.com.duxusdesafio.repository.TimeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CadastroService {

    private final IntegranteRepository integranteRepository;
    private final TimeRepository timeRepository;
    private final ComposicaoTimeRepository composicaoTimeRepository;

    public CadastroService(IntegranteRepository integranteRepository, 
                           TimeRepository timeRepository, 
                           ComposicaoTimeRepository composicaoTimeRepository) {
        this.integranteRepository = integranteRepository;
        this.timeRepository = timeRepository;
        this.composicaoTimeRepository = composicaoTimeRepository;
    }

    @Transactional
    public Integrante cadastrarIntegrante(IntegranteDTO dto) {
        Integrante integrante = new Integrante();
        integrante.setNome(dto.nome());
        integrante.setFuncao(dto.funcao());
        return integranteRepository.save(integrante);
    }

    @Transactional
    public Time cadastrarTime(TimeDTO dto) {
        // 1. Criar e salvar a entidade Time
        Time time = new Time();
        time.setNomeDoClube(dto.nomeDoClube());
        time.setData(dto.data());
        final Time timeSalvo = timeRepository.save(time);

        // 2. Buscar os integrantes e criar as composições
        List<ComposicaoTime> composicoes = dto.integrantesIds().stream()
                .map(id -> {
                    Integrante integrante = integranteRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Integrante não encontrado com ID: " + id));
                    
                    ComposicaoTime composicao = new ComposicaoTime();
                    composicao.setTime(timeSalvo);
                    composicao.setIntegrante(integrante);
                    return composicao;
                })
                .collect(Collectors.toList());

        // 3. Salvar as composições
        composicaoTimeRepository.saveAll(composicoes);
        
        // 4. Atualizar a lista de composições no objeto Time e retornar
        timeSalvo.setComposicaoTime(composicoes);
        return timeSalvo;
    }
}
