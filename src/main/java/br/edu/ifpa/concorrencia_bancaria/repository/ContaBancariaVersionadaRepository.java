package br.edu.ifpa.concorrencia_bancaria.repository;

import br.edu.ifpa.concorrencia_bancaria.model.ContaBancariaVersionada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaBancariaVersionadaRepository extends JpaRepository<ContaBancariaVersionada, Long> {

}
