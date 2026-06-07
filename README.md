# springboot-06-2026

Este projeto é uma implementação prática para demonstrar e resolver problemas de concorrência (especificamente o problema de Lost Update/Atualização Perdida) em um sistema transacional utilizando Spring Boot, JPA/Hibernate e banco de dados H2.

**Aluno:** Laura Ramos Alves
**Matrícula:** 202407905454

- **Parte 1:** Implementação do cenário sem bloqueio (ContaBancaria e classes auxiliares).
- **Parte 2:** Implementação da solução com Lock Otimista (ContaBancariaVersionada e classes auxiliares).

---

## Instruções para rodar a aplicação

### Pré-requisitos

- Java 17 ou superior
- Maven
- Apache JMeter

### Passo a passo para execução

1. **Clone o repositório:**

```bash
   git clone https://github.com/lauraonline/springboot-06-2026.git
   cd springboot-06-2026
```

2. **Inicie a aplicação Spring Boot:**
   Você pode rodar diretamente pela sua IDE (executando a classe `ConcorrenciaBancariaApplication`).

3. **Acesso ao Banco de Dados (H2):**
   - A aplicação cria automaticamente as tabelas e insere os dados iniciais pelo arquivo `data.sql`.
   - Abra o navegador e acesse: `http://localhost:8080/h2-console`
   - **JDBC URL:** `jdbc:h2:mem:testdb`
   - **User Name:** `sa`
   - **Password:** _(deixe em branco)_

---

## Testes de Concorrência (JMeter)

O arquivo de cenários de teste exigido pelo trabalho está salvo na raiz deste repositório com o nome **`cenarios-teste.jmx`**.

Para executá-lo:

1. Abra o Apache JMeter.
2. Vá em `File > Open` e selecione o arquivo `cenarios-teste.jmx` dentro da raiz deste repositório.
3. Certifique-se de que a aplicação Spring Boot está rodando.
4. Clique no ícone ao lado dos dois Thread Groups para abrí-los. Saiba a diferença entre o Thread Group não-versionado e o versionado.
5. Clique em `"Test Plan"`, e clique no botão de `"Play" (Start)` no JMeter para disparar as 100 requisições simultâneas de depósito e saque para os dois tipos de conta.
6. Dentro de cada Thread Group, analise o sumário em `"Summary Report"`.
7. No banco de dados, analise os dois tipos de conta bancária.

---

## Relatório de Conclusão: Análise Comparativa

O teste do JMeter simulou 100 usuários realizando operações simultâneas de depósito (R$ 50,00) e saque (R$ 50,00) em uma conta com saldo inicial de R$ 1.000,00, para cada cenário (conta não-versionada e conta versionada).

### Parte 1: O Problema (Conta sem controle de concorrência)

No primeiro cenário, os endpoints `/contas/{id}/deposito` e `/contas/{id}/saque` utilizaram apenas a anotação `@Transactional` padrão.

- **Resultado:** O JMeter registrou 100% de sucesso (HTTP Status 200 OK) em todas as requisições. No entanto, ao consultar o banco de dados, **o saldo final estava completamente inconsistente** (diferente de R$ 1.000,00).
- **Por que ocorreu?** Isso ocorreu devido ao fenômeno conhecido como **_Lost Update_ (Atualização Perdida)**. Múltiplas threads leram o mesmo saldo base no banco de dados ao mesmo tempo, calcularam o novo valor em memória e salvaram por cima umas das outras de forma silenciosa, ignorando as operações paralelas.

> ![Print ContaBancaria JMeter](/media/ContaBancariaJM.png)
> ![Print ContaBancaria H2](/media/ContaBancariaH2.png)

### Parte 2: A Solução (Conta Versionada com Lock Otimista)

No segundo cenário, os endpoints `/contas-versionadas/{id}/...` operaram sobre uma entidade com um atributo anotado com `@Version` (`Optimistic Locking`).

- **Resultado:** O JMeter registrou várias requisições com falha, retornando **HTTP Status 409 Conflict**. Ao contrário da Parte 1, a discrepância nos sucessos se refletiu perfeitamente no banco de dados. Por exemplo: se 5 saques a mais deram sucesso em relação aos depósitos, o saldo final fechou exatamente em R$ 750,00 (1000 - 250).
- **Por que ocorreu?** O Spring/Hibernate passou a validar a versão do registro antes de cada `UPDATE`. Quando a transação percebia que o registro havia sido alterado por outra thread (versões diferentes), ela **abortava a operação**, lançando a exceção `ObjectOptimisticLockingFailureException`.
- **Conclusão:** O sistema tratou o erro retornando um Status 409 e impediu a atualização perdida. A integridade financeira foi 100% preservada, pois o banco de dados calculou cada centavo perfeitamente de acordo com as operações que ele efetivamente permitiu passar (Status 200).

> ![Print ContaBancariaVersionada JMeter](/media/ContaBancariaVersionadaJM.png)
> ![Print ContaBancariaVersionada H2](/media/ContaBancariaVersionadaH2.png)
