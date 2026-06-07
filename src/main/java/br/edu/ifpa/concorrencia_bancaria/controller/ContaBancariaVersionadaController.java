package br.edu.ifpa.concorrencia_bancaria.controller;

import br.edu.ifpa.concorrencia_bancaria.dto.OperacaoDTO;
import br.edu.ifpa.concorrencia_bancaria.service.ContaBancariaVersionadaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contas-versionadas")
public class ContaBancariaVersionadaController {

    private final ContaBancariaVersionadaService service;

    public ContaBancariaVersionadaController(ContaBancariaVersionadaService service) {
        this.service = service;
    }

    @PostMapping("/{id}/deposito")
    public ResponseEntity<String> depositar(@PathVariable Long id, @RequestBody OperacaoDTO dto) {
        try {
            service.depositar(id, dto.getValor());
            return ResponseEntity.ok("Depósito realizado com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/saque")
    public ResponseEntity<String> sacar(@PathVariable Long id, @RequestBody OperacaoDTO dto) {
        try {
            service.sacar(id, dto.getValor());
            return ResponseEntity.ok("Saque realizado com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
