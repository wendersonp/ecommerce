package com.wendersonp.processor.domain.models;

import com.wendersonp.processor.domain.enumeration.OrderStatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "VENDA")
public class SaleOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "venda_seq_gen")
    @SequenceGenerator(name = "venda_seq_gen", allocationSize = 1, sequenceName = "venda_seq")
    @Column(nullable = false, columnDefinition = "DECIMAL(38, 0)")
    private BigInteger id;

    @Column(nullable = false, columnDefinition = "VARCHAR(100)")
    private String canal;

    private int codigoEmpresa;

    private int codigoLoja;

    private int numeroPdv;

    @Column(nullable = false, columnDefinition = "VARCHAR(38)")
    private String numeroPedido;

    @Column(nullable = false, columnDefinition = "VARCHAR(38)")
    private String numeroOrdemExterno;

    @Column(nullable = false, columnDefinition = "DECIMAL(38,2)")
    private BigDecimal valorTotal;

    @Column(nullable = false, columnDefinition = "DECIMAL(38,0)")
    private BigInteger qtdItem;

    @Column(nullable = false, columnDefinition = "CLOB")
    private String vendaRequest;

    @Column(nullable = false)
    private LocalDateTime dataAtualizacao;

    @Column(nullable = false)
    private LocalDateTime dataRequisicao;

    @Column(columnDefinition = "VARCHAR(44)")
    private String chaveNfe;

    @Column(columnDefinition = "DECIMAL(38,0)")
    private BigInteger numeroNota;

    private LocalDateTime dataEmissao;

    @Column(columnDefinition = "CLOB")
    private String pdf;

    @Column(columnDefinition = "VARCHAR(100)", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum situacao;

    @Column(columnDefinition = "VARCHAR(255)")
    private String motivo;
}
