package com.project.myfinances.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.myfinances.dto.AtualizaStatusDTO;
import com.project.myfinances.dto.LancamentoDTO;
import com.project.myfinances.exceptions.RegraNegocioException;
import com.project.myfinances.model.entity.Lancamento;
import com.project.myfinances.model.entity.Usuario;
import com.project.myfinances.model.enums.StatusLancamento;
import com.project.myfinances.model.enums.TipoLancamento;
import com.project.myfinances.service.LancamentoService;
import com.project.myfinances.service.UsuarioService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = LancamentoResource.class)
@AutoConfigureMockMvc
public class LancamentoResourceTest {

    private static final String API = "/api/lancamentos";
    private static final String JSON = MediaType.APPLICATION_JSON_VALUE;
    private static final Integer ANO = 2019;
    private static final Integer MES = 11;
    private static final String DESCRICAO = "Pagamento do INSS";
    private static final BigDecimal VALOR = BigDecimal.TEN;
    private static final StatusLancamento STATUS = StatusLancamento.PENDENTE;
    private static final TipoLancamento TIPO = TipoLancamento.RECEITA;
    private static final Long ID_USUARIO = 1L;
    private static final Long ID_LANCAMENTO = 1L;

    @Autowired
    MockMvc mvc;

    @MockBean
    LancamentoService service;

    @MockBean
    UsuarioService usuarioService;

    @Test
    void deveBuscarUmLancamento() throws Exception {
        //given
        Usuario usuario = criarUsuario();
        Lancamento lancamento = criarLancamento(usuario);
        List<Lancamento> lista = List.of(lancamento, lancamento);
        Lancamento filtro = Lancamento.builder().usuario(usuario).build();

        //when
        when(usuarioService.obterPorId(anyLong())).thenReturn(Optional.of(usuario));
        when(service.buscar(filtro)).thenReturn(lista);

        //then
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(API.concat("?usuario={id}"), ID_USUARIO)
                .accept(JSON)
                .contentType(JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.[0].id").value(ID_LANCAMENTO))
                .andExpect(jsonPath("$.[0].descricao").value(DESCRICAO))
                .andExpect(jsonPath("$.[0].mes").value(MES))
                .andExpect(jsonPath("$.[0].ano").value(ANO))
                .andExpect(jsonPath("$.[0].valor").value(VALOR))
                .andExpect(jsonPath("$.[0].tipo").value(TIPO.name()))
                .andExpect(jsonPath("$.[0].status").value(STATUS.name()));
    }

    @Test
    void deveRetornarNotFoundAoTentarBuscarUmLancamentoDeUmUsuario() throws Exception {
        //when
        when(usuarioService.obterPorId(anyLong())).thenReturn(Optional.empty());

        //then
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(API.concat("?usuario={id}"), ID_USUARIO)
                .contentType(JSON)
                .accept(JSON);

        mvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Usuário não encontrado."));
    }

    @Test
    void deveAtualizarOStatusDeUmLancamento() throws Exception {
        //given
        AtualizaStatusDTO dto = criarStatusDTO();
        Usuario usuario = criarUsuario();
        Lancamento lancamento = criarLancamento(usuario);

        Lancamento lancamentoASalvar = criarLancamento(usuario);
        lancamentoASalvar.setStatus(StatusLancamento.CANCELADO);

        String conteudoJson = new ObjectMapper().writeValueAsString(dto);

        //when
        when(service.obterPorId(anyLong())).thenReturn(Optional.of(lancamento));
        when(service.atualizar(any(Lancamento.class))).thenReturn(lancamentoASalvar);

        //then
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(API.concat("/{id}/atualizar-status"), ID_LANCAMENTO)
                .contentType(JSON)
                .accept(JSON)
                .content(conteudoJson);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(ID_LANCAMENTO))
                .andExpect(jsonPath("descricao").value(DESCRICAO))
                .andExpect(jsonPath("mes").value(MES))
                .andExpect(jsonPath("ano").value(ANO))
                .andExpect(jsonPath("valor").value(VALOR))
                .andExpect(jsonPath("tipo").value(TIPO.name()))
                .andExpect(jsonPath("status").value(StatusLancamento.CANCELADO.name()));
    }

    @Test
    void deveRetornarBadRequestAoTentarAtualizarUmStatusDeLancamento() throws Exception {
        //given
        AtualizaStatusDTO dto = criarStatusDTO();
        Usuario usuario = criarUsuario();
        Lancamento lancamento = criarLancamento(usuario);

        String conteudoJson = new ObjectMapper().writeValueAsString(dto);

        //when
        when(service.obterPorId(anyLong())).thenReturn(Optional.of(lancamento));
        doThrow(RegraNegocioException.class).when(service).atualizar(lancamento);

        //then
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(API.concat("/{id}/atualizar-status"), ID_LANCAMENTO)
                .accept(JSON)
                .contentType(JSON)
                .content(conteudoJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarNotFoundAoTentarAtualizarOStatusDeUmLancamento() throws Exception {
        //given
        AtualizaStatusDTO dto = criarStatusDTO();

        String conteudoJson = new ObjectMapper().writeValueAsString(dto);

        //when
        when(service.obterPorId(anyLong())).thenReturn(Optional.empty());

        //then
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(API.concat("/{id}/atualizar-status"), ID_LANCAMENTO)
                .accept(JSON)
                .contentType(JSON)
                .content(conteudoJson);

        mvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Lançamento não encontrado."));
    }

    @Test
    void deveRetornarBadRequestAoTentarAtualizarUmStatusDeLancamentoComStatusInvalido() throws Exception {
        //given
        Usuario usuario = criarUsuario();
        Lancamento lancamento = criarLancamento(usuario);

        //when
        when(service.obterPorId(anyLong())).thenReturn(Optional.of(lancamento));

        //then
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(API.concat("/{id}/atualizar-status"), ID_LANCAMENTO)
                .accept(JSON)
                .contentType(JSON)
                .content("RANDOM");

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarNotFoundAoTentarDeletarUmLancamento() throws Exception {
        //when
        when(service.obterPorId(anyLong())).thenReturn(Optional.empty());

        //then
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(API.concat("/{id}"), ID_LANCAMENTO)
                .content(JSON)
                .contentType(JSON);

        mvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Lançamento não encontrado"));
    }

    @Test
    void deveRetornarBadRequestAoTentarDeletarUmLancamento() throws Exception {
        //given
        Usuario usuario = criarUsuario();
        Lancamento lancamento = criarLancamento(usuario);

        //when
        when(service.obterPorId(anyLong())).thenReturn(Optional.of(lancamento));
        doThrow(RegraNegocioException.class).when(service).deletar(lancamento);

        //then
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(API.concat("/{id}"), ID_LANCAMENTO)
                .content(JSON)
                .contentType(JSON);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveDeletarUmLancamento() throws Exception {
        //given
        Usuario usuario = criarUsuario();
        Lancamento lancamento = criarLancamento(usuario);

        //when
        when(service.obterPorId(anyLong())).thenReturn(Optional.of(lancamento));

        //then
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(API.concat("/{id}"), ID_LANCAMENTO)
                .contentType(JSON)
                .accept(JSON);

        mvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    void deveSalvarUmLancamento() throws Exception {
        //given
        Usuario usuario = criarUsuario();
        Lancamento lancamentoASalvar = criarLancamentoASalvar(usuario);
        LancamentoDTO dto = criarDTO();
        Lancamento lancamento = criarLancamento(usuario);

        String conteudoJson = new ObjectMapper().writeValueAsString(dto);

        //when
        when(service.salvar(lancamentoASalvar)).thenReturn(lancamento);
        when(usuarioService.obterPorId(anyLong())).thenReturn(Optional.of(usuario));

        //then
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API)
                .contentType(JSON)
                .accept(JSON)
                .content(conteudoJson);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(ID_LANCAMENTO))
                .andExpect(jsonPath("descricao").value(DESCRICAO))
                .andExpect(jsonPath("mes").value(MES))
                .andExpect(jsonPath("ano").value(ANO))
                .andExpect(jsonPath("valor").value(VALOR))
                .andExpect(jsonPath("tipo").value(TIPO.name()))
                .andExpect(jsonPath("status").value(STATUS.name()));
    }

    @Test
    void deveRetornarBadRequestAoTentarSalvarUmLancamento() throws Exception {
        //given
        LancamentoDTO dto = criarDTO();
        Usuario usuario = criarUsuario();
        Lancamento lancamentoASalvar = criarLancamentoASalvar(usuario);

        String conteudoJson = new ObjectMapper().writeValueAsString(dto);

        //when
        when(service.salvar(lancamentoASalvar)).thenThrow(RegraNegocioException.class);

        //then
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API)
                .contentType(JSON)
                .accept(JSON)
                .content(conteudoJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveAtualizarUmLancamento() throws Exception {
        //given
        Usuario usuario = criarUsuario();
        LancamentoDTO dto = criarDTO();
        Lancamento lancamento = criarLancamento(usuario);

        Lancamento lancamentoAAtualizar = criarLancamentoASalvar(usuario);
        lancamentoAAtualizar.setId(ID_LANCAMENTO);
        lancamentoAAtualizar.setAno(2021);
        lancamentoAAtualizar.setTipo(TipoLancamento.DESPESA);
        lancamentoAAtualizar.setStatus(StatusLancamento.PENDENTE);

        String conteudoJson = new ObjectMapper().writeValueAsString(dto);

        //when
        when(service.obterPorId(anyLong())).thenReturn(Optional.of(lancamento));
        when(usuarioService.obterPorId(anyLong())).thenReturn(Optional.of(usuario));
        when(service.atualizar(any(Lancamento.class))).thenReturn(lancamentoAAtualizar);

        //then
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(API.concat("/{id}"), ID_LANCAMENTO)
                .contentType(JSON)
                .accept(JSON)
                .content(conteudoJson);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(ID_LANCAMENTO))
                .andExpect(jsonPath("descricao").value(DESCRICAO))
                .andExpect(jsonPath("mes").value(MES))
                .andExpect(jsonPath("ano").value(2021))
                .andExpect(jsonPath("valor").value(VALOR))
                .andExpect(jsonPath("tipo").value(TipoLancamento.DESPESA.name()))
                .andExpect(jsonPath("status").value(STATUS.name()));
    }

    @Test
    void deveRetornarNotFoundAoTentarAtualizarUmLancamento() throws Exception {
        //given
        LancamentoDTO dto = criarDTO();

        String conteudoJson = new ObjectMapper().writeValueAsString(dto);

        //when
        when(service.obterPorId(anyLong())).thenReturn(Optional.empty());

        //then
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(API.concat("/{id}"), ID_LANCAMENTO)
                .accept(JSON)
                .contentType(JSON)
                .content(conteudoJson);

        mvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Lançamento não encontrado"));
    }

    @Test
    void deveRetornarBadRequestAoTentarAtualizarUmLancamento() throws Exception {
        //given
        Usuario usuario = criarUsuario();
        LancamentoDTO dto = criarDTO();
        Lancamento lancamento = criarLancamento(usuario);

        String conteudoJson = new ObjectMapper().writeValueAsString(dto);

        //when
        when(service.obterPorId(anyLong())).thenReturn(Optional.of(lancamento));
        when(usuarioService.obterPorId(anyLong())).thenReturn(Optional.of(usuario));
        when(service.atualizar(any(Lancamento.class))).thenThrow(RegraNegocioException.class);

        //then
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(API.concat("/{id}"), ID_LANCAMENTO)
                .contentType(JSON)
                .accept(JSON)
                .content(conteudoJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    private Usuario criarUsuario() {
        return Usuario.builder().id(ID_USUARIO).nome("NOME")
                .email("EMAIL").senha("SENHA").build();
    }

    private LancamentoDTO criarDTO() {
        return LancamentoDTO.builder().ano(ANO).mes(MES)
                .usuario(ID_USUARIO).descricao(DESCRICAO)
                .tipo(TIPO.name()).valor(VALOR).build();
    }

    private AtualizaStatusDTO criarStatusDTO() {
        return AtualizaStatusDTO.builder()
                .status(StatusLancamento.CANCELADO.name()).build();
    }

    private Lancamento criarLancamentoASalvar(Usuario usuario) {
        return Lancamento.builder()
                .usuario(usuario).mes(MES).ano(ANO).valor(VALOR)
                .descricao(DESCRICAO).tipo(TIPO).build();
    }

    private Lancamento criarLancamento(Usuario usuario) {
        return Lancamento.builder().id(ID_LANCAMENTO)
                .mes(MES).ano(ANO).usuario(usuario).valor(VALOR)
                .descricao(DESCRICAO).tipo(TIPO).status(STATUS)
                .dataCadastro(LocalDateTime.now()).build();
    }
}
