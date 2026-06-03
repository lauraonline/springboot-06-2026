package br.edu.ifpa.concorrencia_bancaria.repository;

import br.edu.ifpa.concorrencia_bancaria.model.ContaBancaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// controller
@Repository
public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, Long> {

}
