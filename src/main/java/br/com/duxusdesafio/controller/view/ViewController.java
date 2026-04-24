package br.com.duxusdesafio.controller.view;

import br.com.duxusdesafio.repository.IntegranteRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/telas")
public class ViewController {

    private final IntegranteRepository integranteRepository;

    public ViewController(IntegranteRepository integranteRepository) {
        this.integranteRepository = integranteRepository;
    }

    @GetMapping("/integrantes")
    public String telaIntegrantes() {
        return "integrantes"; // Procura por src/main/resources/templates/integrantes.html
    }

    @GetMapping("/time")
    public String telaTime(Model model) {
        // Para montar o time, precisamos listar todos os integrantes do banco
        model.addAttribute("integrantes", integranteRepository.findAll());
        return "time"; // Procura por src/main/resources/templates/time.html
    }
}
