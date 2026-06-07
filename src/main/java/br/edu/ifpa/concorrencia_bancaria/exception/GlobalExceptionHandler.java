package br.edu.ifpa.concorrencia_bancaria.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// classe lida com erros em tempo de execução
@ControllerAdvice
public class GlobalExceptionHandler {

    // erro ObjectOptimisticLockingFailureException lida especificamente com o erro
    // de requisições simultâneas
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<String> handleOptimisticLockingFailure(ObjectOptimisticLockingFailureException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Conflito: Outra transação alterou esta conta ao mesmo tempo. Espere um momento e tente novamente.");
    }

    // erros gerais
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}