package com.project.myfinances.repository;

import com.project.myfinances.model.entity.Lancamento;
import com.project.myfinances.model.enums.StatusLancamento;
import com.project.myfinances.model.enums.TipoLancamento;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest //para teste de integração
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//para não sobreescrever as configurações de testr
public class LancamentoRepositoryTest {

    @Autowired
    LancamentoRepository repository;

    @Autowired
    TestEntityManager entityManager;

    public static Lancamento criarLancamento() {
        return Lancamento.builder()
                .ano(2019)
                .mes(1)
                .descricao("Conta de agua")
                .valor(BigDecimal.TEN)
                .tipo(TipoLancamento.RECEITA)
                .status(StatusLancamento.PENDENTE)
                .dataCadastro(LocalDateTime.now())
                .build();
    }

    @Test
    void deveSalvarUmLancamento() {
        //given
        Lancamento lancamento = criarLancamento();

        //when
        lancamento = repository.save(lancamento);

        //then
        assertThat(lancamento.getId()).isNotNull();
    }

    @Test
    void deveDeletarUmLancamento() {
        //given
        Lancamento lancamento = criarLancamento();
        entityManager.persist(lancamento);

        //when
        lancamento = entityManager.find(Lancamento.class, lancamento.getId());

        repository.delete(lancamento);

        Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());

        //then
        assertThat(lancamentoInexistente).isNull();
    }

    @Test
    void deveAtualizarUmLancamento() {
        //given
        Lancamento lancamento = criarLancamento();
        entityManager.persist(lancamento);
//        repository.save(lancamento);

        //when
        lancamento.setAno(2020);
        lancamento.setStatus(StatusLancamento.EFETIVADO);

        repository.save(lancamento);

        Lancamento lancamentoAtualizado = entityManager.find(Lancamento.class, lancamento.getId());
//         Optional<Lancamento> lancamentoAtualizado = repository.findById(lancamento.getId());

        //then
        assertThat(lancamentoAtualizado.getAno()).isEqualTo(2020);
        assertThat(lancamentoAtualizado.getStatus()).isEqualTo(StatusLancamento.EFETIVADO);
    }

    @Test
    void deveBuscarUmLancamentoPorId() {
        //given
        Lancamento lancamento = criarLancamento();
        entityManager.persist(lancamento);

        //when
        Optional<Lancamento> lancamentoEncontrado = repository.findById(lancamento.getId());

        //then
        assertThat(lancamentoEncontrado.isPresent()).isTrue();
    }
}
