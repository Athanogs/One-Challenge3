package com.aluracursos.forohub.infra.security;

import jakarta.validation.constraints.NotBlank;

public record DatosAutenticacionUsuario(
        @NotBlank String correoElectronico,
        @NotBlank String contrasena
) {
}