package br.edu.ifpa.concorrencia_bancaria.dto;

import java.math.BigDecimal;

// data transfer object
public class OperacaoDTO {
    private BigDecimal valor;

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
