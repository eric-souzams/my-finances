package com.project.myfinances.resource;

import com.project.myfinances.dto.LoginDTO;
import com.project.myfinances.dto.TokenDTO;
import com.project.myfinances.dto.UsuarioDTO;

import com.project.myfinances.exceptions.ErroAutenticacaoException;
import com.project.myfinances.exceptions.RegraNegocioException;
import com.project.myfinances.model.entity.Usuario;
import com.project.myfinances.service.JwtService;
import com.project.myfinances.service.LancamentoService;
import com.project.myfinances.service.UsuarioService;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/usuarios")
public class UsuarioResource {

    private UsuarioService service;
    private LancamentoService lancamentoService;
    private JwtService jwtService;

    @PostMapping(value = "/autenticar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> autenticar(@RequestBody LoginDTO dto) {
        try {
            Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());

            TokenDTO token = TokenDTO.builder()
                    .token(jwtService.generateToken(usuarioAutenticado))
                    .build();

            return ResponseEntity.ok(token);
        } catch (ErroAutenticacaoException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> salvar(@RequestBody UsuarioDTO dto) {
        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(dto.getSenha())
                .build();

        try {
            Usuario usuarioCriado = service.salvarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriado);
        } catch (RegraNegocioException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping(value = "/{id}/saldo")
    public ResponseEntity<?> obterSaldo(@PathVariable("id") Long id) {
        Optional<Usuario> usuario = service.obterPorId(id);

        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        BigDecimal saldo = lancamentoService.obterSaldoPorUsuario(id);

        return ResponseEntity.ok(saldo);
    }

}
