package com.project.myfinances.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LancamentoDTO {

    private Long id;

    private String descricao;

    private Integer mes;

    private Integer ano;

    private BigDecimal valor;

    private Long usuario;

    private String tipo;

    private String status;

    private LocalDateTime dataCadastro;

}
