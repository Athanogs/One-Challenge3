package com.aluracursos.forohub.controller;

import com.aluracursos.forohub.domain.usuario.Usuario;
import com.aluracursos.forohub.infra.security.DatosAutenticacionUsuario;
import com.aluracursos.forohub.infra.security.DatosTokenJWT;
import com.aluracursos.forohub.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class AutenticacionController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AutenticacionController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<DatosTokenJWT> autenticar(@RequestBody @Valid DatosAutenticacionUsuario datos) {
        var authToken = new UsernamePasswordAuthenticationToken(
                datos.correoElectronico(),
                datos.contrasena()
        );

        var authentication = authenticationManager.authenticate(authToken);
        var usuario = (Usuario) authentication.getPrincipal();
        var tokenJWT = tokenService.generarToken(usuario);

        return ResponseEntity.ok(new DatosTokenJWT(tokenJWT));
    }
}