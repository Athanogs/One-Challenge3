package com.aluracursos.forohub.controller;

import com.aluracursos.forohub.domain.curso.Curso;
import com.aluracursos.forohub.domain.topico.DatosRegistroTopico;
import com.aluracursos.forohub.domain.topico.Topico;
import com.aluracursos.forohub.domain.usuario.Usuario;
import com.aluracursos.forohub.repository.CursoRepository;
import com.aluracursos.forohub.repository.TopicoRepository;
import com.aluracursos.forohub.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.aluracursos.forohub.domain.topico.DatosListadoTopico;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.aluracursos.forohub.domain.topico.DatosDetalleTopico;
import com.aluracursos.forohub.domain.topico.DatosActualizacionTopico;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    private final TopicoRepository topicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;

    public TopicoController(TopicoRepository topicoRepository,
                            UsuarioRepository usuarioRepository,
                            CursoRepository cursoRepository) {
        this.topicoRepository = topicoRepository;
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody @Valid DatosRegistroTopico datos) {

        if (topicoRepository.existsByTituloAndMensaje(datos.titulo(), datos.mensaje())) {
            return ResponseEntity.badRequest().body("Ya existe un tópico con el mismo título y mensaje.");
        }

        Usuario autor = usuarioRepository.findById(datos.autor())
                .orElseThrow(() -> new RuntimeException("Autor no encontrado."));

        Curso curso = cursoRepository.findById(datos.curso())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado."));

        Topico topico = new Topico(
                datos.titulo(),
                datos.mensaje(),
                autor,
                curso
        );

        topicoRepository.save(topico);

        return ResponseEntity.ok("Tópico registrado correctamente.");
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<DatosListadoTopico>> listar() {
        var lista = topicoRepository.findAll().stream()
                .map(DatosListadoTopico::new)
                .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<?> detallar(@PathVariable Long id) {
        var topicoOptional = topicoRepository.findById(id);

        if (topicoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var datosDetalle = new DatosDetalleTopico(topicoOptional.get());
        return ResponseEntity.ok(datosDetalle);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @RequestBody @Valid DatosActualizacionTopico datos) {

        var topicoOptional = topicoRepository.findById(id);

        if (!topicoOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        if (topicoRepository.existsByTituloAndMensajeAndIdNot(datos.titulo(), datos.mensaje(), id)) {
            return ResponseEntity.badRequest().body("Ya existe otro tópico con el mismo título y mensaje.");
        }

        var autorOptional = usuarioRepository.findById(datos.autor());
        if (!autorOptional.isPresent()) {
            return ResponseEntity.badRequest().body("El autor indicado no existe.");
        }

        var cursoOptional = cursoRepository.findById(datos.curso());
        if (!cursoOptional.isPresent()) {
            return ResponseEntity.badRequest().body("El curso indicado no existe.");
        }

        var topico = topicoOptional.get();
        topico.actualizarInformaciones(
                datos.titulo(),
                datos.mensaje(),
                autorOptional.get(),
                cursoOptional.get()
        );

        topicoRepository.save(topico);

        return ResponseEntity.ok(new DatosDetalleTopico(topico));
    }


    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        var topicoOptional = topicoRepository.findById(id);

        if (!topicoOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        topicoRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}