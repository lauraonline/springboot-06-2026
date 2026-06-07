package br.edu.ifpa.concorrencia_bancaria.service;

import br.edu.ifpa.concorrencia_bancaria.model.ContaBancariaVersionada;
import br.edu.ifpa.concorrencia_bancaria.repository.ContaBancariaVersionadaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ContaBancariaVersionadaService {
    private final ContaBancariaVersionadaRepository repository;

    public ContaBancariaVersionadaService(ContaBancariaVersionadaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void depositar(Long id, BigDecimal valor) {
        // select por id
        ContaBancariaVersionada conta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        // update saldo
        BigDecimal novoSaldo = conta.getSaldo().add(valor);
        conta.setSaldo(novoSaldo);

        repository.save(conta);
    }

    @Transactional
    public void sacar(Long id, BigDecimal valor) {
        ContaBancariaVersionada conta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        // validação para não permitir saldo negativo
        if (conta.getSaldo().compareTo(valor) < 0) {
            throw new RuntimeException("Saldo insuficiente para o saque solicitado");
        }

        BigDecimal novoSaldo = conta.getSaldo().subtract(valor);
        conta.setSaldo(novoSaldo);

        repository.save(conta);
    }
}