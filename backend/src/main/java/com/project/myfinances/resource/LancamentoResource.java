package com.project.myfinances.resource;

import com.project.myfinances.dto.AtualizaStatusDTO;
import com.project.myfinances.dto.LancamentoDTO;
import com.project.myfinances.exceptions.RegraNegocioException;
import com.project.myfinances.model.entity.Lancamento;
import com.project.myfinances.model.entity.Usuario;
import com.project.myfinances.model.enums.StatusLancamento;
import com.project.myfinances.model.enums.TipoLancamento;
import com.project.myfinances.service.LancamentoService;
import com.project.myfinances.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/lancamentos")
public class LancamentoResource {

    private LancamentoService service;
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<?> buscar(@RequestParam(value = "descricao", required = false) String descricao,
                                    @RequestParam(value = "mes", required = false) Integer mes,
                                    @RequestParam(value = "ano", required = false) Integer ano,
                                    @RequestParam(value = "tipo", required = false) String tipo,
                                    @RequestParam(value = "usuario") Long idUsuario) {

        Lancamento filtro = new Lancamento();
        filtro.setDescricao(descricao);
        filtro.setAno(ano);
        filtro.setMes(mes);
        if(tipo != null) {
            filtro.setTipo(TipoLancamento.valueOf(tipo));
        }

        Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }
        filtro.setUsuario(usuario.get());

        List<Lancamento> lancamentos = service.buscar(filtro);

        return ResponseEntity.ok(lancamentos);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> salvar(@RequestBody LancamentoDTO dto) {
        try {
            Lancamento entidade = converter(dto);
            entidade = service.salvar(entidade);

            return ResponseEntity.status(HttpStatus.CREATED).body(entidade);
        } catch (RegraNegocioException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO dto) {
        return service.obterPorId(id).map(entidade -> {
            try {
                Lancamento lancamento = converter(dto);
                lancamento.setId(entidade.getId());

                lancamento = service.atualizar(lancamento);
                return ResponseEntity.ok(lancamento);
            } catch (RegraNegocioException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity<>("Lançamento não encontrado", HttpStatus.NOT_FOUND));
    }

    @PutMapping(value = "/{id}/atualizar-status")
    public ResponseEntity<?> atualizarStatus(@PathVariable("id") Long id, @RequestBody AtualizaStatusDTO dto) {
        return service.obterPorId(id).map(entidade -> {
            if (dto.getStatus() == null || dto.getStatus().trim().equals("")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não foi possível atualizar o status do lançamento. Informe um status válido.");
            }

            StatusLancamento statusLancamento = StatusLancamento.valueOf(dto.getStatus());

            try {
                entidade.setStatus(statusLancamento);
                entidade = service.atualizar(entidade);
                return ResponseEntity.ok(entidade);
            } catch (RegraNegocioException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity<>("Lançamento não encontrado.", HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deletar(@PathVariable("id") Long id) {
        return service.obterPorId(id).map(entidade -> {
           try {
               service.deletar(entidade);
               return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
           } catch (RegraNegocioException e) {
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
           }
        }).orElseGet(() -> new ResponseEntity<>("Lançamento não encontrado", HttpStatus.NOT_FOUND));
    }

    private Lancamento converter(LancamentoDTO dto) {
        Lancamento lancamento = new Lancamento();

        lancamento.setId(dto.getId());
        lancamento.setDescricao(dto.getDescricao());
        lancamento.setAno(dto.getAno());
        lancamento.setMes(dto.getMes());
        lancamento.setValor(dto.getValor());

        Usuario usuario = usuarioService
                .obterPorId(dto.getUsuario())
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado."));

        lancamento.setUsuario(usuario);
        lancamento.setDataCadastro(dto.getDataCadastro());

        if (dto.getTipo() != null) {
            lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
        }

        if (dto.getStatus() != null) {
            lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
        }

        return lancamento;
    }

}
