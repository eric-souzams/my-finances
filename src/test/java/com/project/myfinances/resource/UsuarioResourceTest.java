package com.project.myfinances.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.myfinances.dto.UsuarioDTO;
import com.project.myfinances.exceptions.ErroAutenticacaoException;
import com.project.myfinances.exceptions.RegraNegocioException;
import com.project.myfinances.model.entity.Usuario;
import com.project.myfinances.service.LancamentoService;
import com.project.myfinances.service.UsuarioService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UsuarioResource.class)
@AutoConfigureMockMvc
public class UsuarioResourceTest {

    private static final String EMAIL = "usuario@email.com";
    private static final String SENHA = "1234";
    private static final Long ID = 1L;
    private static final String NOME = "Usuario";

    private static final String API = "/api/usuarios";
    private static final String JSON = MediaType.APPLICATION_JSON_VALUE;

    @Autowired
    MockMvc mvc;

    @MockBean
    UsuarioService service;

    @MockBean
    LancamentoService lancamentoService;

    @Test
    void deveAutenticarUmUsuario() throws Exception {
        //given
        UsuarioDTO dto = UsuarioDTO.builder().email(EMAIL).senha(SENHA).build();
        Usuario usuario = Usuario.builder().id(ID).nome(NOME).email(EMAIL).senha(SENHA).build();
        String conteudoJson = new ObjectMapper().writeValueAsString(dto);

        //when
        when(service.autenticar(EMAIL, SENHA)).thenReturn(usuario);

        //then
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API.concat("/autenticar"))
                .accept(JSON)
                .contentType(JSON)
                .content(conteudoJson);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(usuario.getId()))
                .andExpect(jsonPath("nome").value(usuario.getNome()))
                .andExpect(jsonPath("email").value(usuario.getEmail()));
    }

    @Test
    void deveRetornarBadRequestAoFalharNaAutenticacao() throws Exception {
        //given
        UsuarioDTO dto = UsuarioDTO.builder().email(EMAIL).senha(SENHA).build();
        String conteudoJson = new ObjectMapper().writeValueAsString(dto);

        //when
        when(service.autenticar(EMAIL, SENHA)).thenThrow(ErroAutenticacaoException.class);

        //then
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API.concat("/autenticar"))
                .accept(JSON)
                .contentType(JSON)
                .content(conteudoJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveCriarUmNovoUsuario() throws Exception {
        //given
        UsuarioDTO dto = UsuarioDTO.builder().nome(NOME).email(EMAIL).senha(SENHA).build();
        Usuario usuario = Usuario.builder().id(ID).nome(NOME).email(EMAIL).senha(SENHA).build();
        String conteudoJson = new ObjectMapper().writeValueAsString(dto);

        //when
        when(service.salvarUsuario(any(Usuario.class))).thenReturn(usuario);

        //then
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API)
                .contentType(JSON)
                .accept(JSON)
                .content(conteudoJson);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(usuario.getId()))
                .andExpect(jsonPath("nome").value(usuario.getNome()))
                .andExpect(jsonPath("email").value(usuario.getEmail()));
    }

    @Test
    void deveRetornarBadRequestAoTentarCriarUmUsuarioInvalido() throws Exception {
        //given
        UsuarioDTO dto = UsuarioDTO.builder().nome(NOME).email(EMAIL).senha(SENHA).build();
        String conteudoJson = new ObjectMapper().writeValueAsString(dto);

        //when
        when(service.salvarUsuario(any(Usuario.class))).thenThrow(RegraNegocioException.class);

        //then
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API)
                .content(JSON)
                .contentType(JSON)
                .content(conteudoJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarOSaldoDoUsuario() throws Exception {
        //given
        Usuario usuario = Usuario.builder().id(ID).nome(NOME).email(EMAIL).senha(SENHA).build();
        BigDecimal valor = BigDecimal.TEN;

        //when
        when(service.obterPorId(ID)).thenReturn(Optional.of(usuario));
        when(lancamentoService.obterSaldoPorUsuario(usuario.getId())).thenReturn(valor);

        //then
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(API.concat("/{id}/saldo"), ID)
                .content(JSON)
                .contentType(JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(valor));
    }
    @Test
    void deveRetornarNotFoundAoTentarBuscarOSaldoDoUsuario() throws Exception {
        //when
        when(service.obterPorId(ID)).thenReturn(Optional.empty());

        //then
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(API.concat("/{id}/saldo"), ID)
                .content(JSON)
                .contentType(JSON);

        mvc.perform(request)
                .andExpect(status().isNotFound());
    }
}
