package br.com.duxusdesafio.dto;

import jakarta.validation.constraints.NotBlank;

public record IntegranteDTO(
        @NotBlank(message = "O nome é obrigatório")
        String nome,
        
        @NotBlank(message = "A função é obrigatória")
        String funcao
) {}
