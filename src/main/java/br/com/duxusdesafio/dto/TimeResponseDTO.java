package br.com.duxusdesafio.dto;

import java.time.LocalDate;
import java.util.List;

public record TimeResponseDTO(
        LocalDate data,
        String clube,
        List<String> integrantes
) {}
