package br.com.duxusdesafio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record TimeDTO(
        @NotBlank(message = "O nome do clube é obrigatório")
        String nomeDoClube,

        @NotNull(message = "A data da escalação é obrigatória")
        LocalDate data,

        @NotEmpty(message = "O time deve ter pelo menos um integrante")
        List<Long> integrantesIds
) {}
