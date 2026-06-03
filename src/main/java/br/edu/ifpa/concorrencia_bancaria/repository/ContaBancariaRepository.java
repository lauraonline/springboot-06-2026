package br.edu.ifpa.concorrencia_bancaria.repository;

import br.edu.ifpa.concorrencia_bancaria.model.ContaBancaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// o repository é uma estrutura do jpa que contém métodos úteis pra interagir com o banco de dados (findById, add, save, etc)
@Repository
public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, Long> {

}
