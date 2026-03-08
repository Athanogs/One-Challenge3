package com.aluracursos.forohub.domain.topico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosActualizacionTopico(

        @NotBlank
        String titulo,

        @NotBlank
        String mensaje,

        @NotNull
        Long autor,

        @NotNull
        Long curso

) {
}