package com.project.myfinances.service;

import com.project.myfinances.exceptions.RegraNegocioException;
import com.project.myfinances.model.entity.Lancamento;
import com.project.myfinances.model.entity.Usuario;
import com.project.myfinances.model.enums.StatusLancamento;
import com.project.myfinances.repository.LancamentoRepository;
import com.project.myfinances.repository.LancamentoRepositoryTest;
import com.project.myfinances.service.impl.LancamentoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {

    @SpyBean //precisa para testar com metodos reais
    LancamentoServiceImpl service;

    @MockBean //simular comportamento
    LancamentoRepository repository;

    @Test
    void deveSalvarUmlancamento() {
        //given
        Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
        Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
        lancamentoSalvo.setId(1L);
        lancamentoSalvo.setStatus(StatusLancamento.EFETIVADO);

        //when
        doNothing().when(service).validar(lancamentoASalvar);
        when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);

        //then
        Lancamento lancamento = service.salvar(lancamentoASalvar);

        assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
        assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.EFETIVADO);
    }

    @Test
    void naoDeveSalvarUmlancamentoQuandoHouverErroDeValidacao() {
        //given
        Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();

        //when
        doThrow(RegraNegocioException.class).when(service).validar(lancamentoASalvar);

        //then
        assertThrows(RegraNegocioException.class, () -> service.salvar(lancamentoASalvar));
        verify(repository, never()).save(lancamentoASalvar);
    }

    @Test
    void deveAtualizarUmLancamentoExistente() {
        //given
        Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
        lancamentoSalvo.setId(1L);

        //when
        doNothing().when(service).validar(lancamentoSalvo);
        when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);

        //then
        service.atualizar(lancamentoSalvo);
        verify(repository, times(1)).save(lancamentoSalvo);
    }

    @Test
    void deveLancarErroAoTentarAtualizarUmLancamentoInexistente() {
        //given
        Lancamento lancamentoInexistente = LancamentoRepositoryTest.criarLancamento();

        //then
        assertThrows(NullPointerException.class, () -> service.atualizar(lancamentoInexistente));
        verify(repository, never()).save(lancamentoInexistente);
    }

    @Test
    void deveDeletarUmLancamento() {
        //given
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(1L);

        //then
        service.deletar(lancamento);
        verify(repository).delete(lancamento);
    }

    @Test
    void deveLancarErroAoTentarDeletarUmLancamentoInexistente() {
        //given
        Lancamento lancamentoInexistente = LancamentoRepositoryTest.criarLancamento();

        //then
        assertThrows(NullPointerException.class, () -> service.deletar(lancamentoInexistente));
        verify(repository, never()).delete(lancamentoInexistente);
    }

    @Test
    void deveFiltrarLancamentos() {
        //given
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(1L);

        List<Lancamento> lista = List.of(lancamento);

        //when
        when(repository.findAll(any(Example.class))).thenReturn(lista);

        //then
        List<Lancamento> resultado = service.buscar(lancamento);

        assertThat(resultado).isNotEmpty().hasSize(1).contains(lancamento);
    }

    @Test
    void deveAtualizarStatusDeUmLancamento() {
        //given
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(1L);
        lancamento.setStatus(StatusLancamento.PENDENTE);

        StatusLancamento novoStatus = StatusLancamento.EFETIVADO;

        //when
        doReturn(lancamento).when(service).atualizar(lancamento);

        //then
        service.atualizarStatus(lancamento, novoStatus);

        assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
        verify(service).atualizar(lancamento);
    }

    @Test
    void deveObterUmLancamentoPorId() {
        //given
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(1L);

        Long ID = 1L;

        //when
        when(repository.findById(ID)).thenReturn(Optional.of(lancamento));

        //then
        Optional<Lancamento> resultado = service.obterPorId(ID);

        assertThat(resultado.isPresent()).isTrue();
        assertThat(resultado.get().getId()).isEqualTo(ID);
    }

    @Test
    void deveRetornarVazioQuandoOLancamentoNaoExiste() {
        //given
        Long ID = 1L;

        //when
        when(repository.findById(ID)).thenReturn(Optional.empty());

        //then
        Optional<Lancamento> resultado = service.obterPorId(ID);

        assertThat(resultado.isEmpty()).isTrue();
    }

    @Test
    void deveLancaErroAoValidarUmLancamento() {
        //given
        Lancamento lancamento = new Lancamento();

        //then
        Throwable erro = catchThrowable(() -> service.validar(lancamento));
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma Descrição válida.");
        lancamento.setDescricao("");
        erro = catchThrowable(() -> service.validar(lancamento));
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma Descrição válida.");

        lancamento.setDescricao("Teste");

        erro = catchThrowable(() -> service.validar(lancamento));
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido.");
        lancamento.setMes(0);
        erro = catchThrowable(() -> service.validar(lancamento));
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido.");
        lancamento.setMes(13);
        erro = catchThrowable(() -> service.validar(lancamento));
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido.");

        lancamento.setMes(1);

        erro = catchThrowable(() -> service.validar(lancamento));
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido.");
        lancamento.setAno(1999);
        erro = catchThrowable(() -> service.validar(lancamento));
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido.");
        lancamento.setAno(202);
        erro = catchThrowable(() -> service.validar(lancamento));
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido.");

        lancamento.setAno(2020);

        erro = catchThrowable(() -> service.validar(lancamento));
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuário válido.");
        lancamento.setUsuario(new Usuario());
        erro = catchThrowable(() -> service.validar(lancamento));
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuário válido.");

        lancamento.getUsuario().setId(1L);

        erro = catchThrowable(() -> service.validar(lancamento));
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido.");
        lancamento.setValor(BigDecimal.ZERO);
        erro = catchThrowable(() -> service.validar(lancamento));
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido.");

        lancamento.setValor(BigDecimal.TEN);

        erro = catchThrowable(() -> service.validar(lancamento));
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um tipo de Lançamento.");
    }
}
