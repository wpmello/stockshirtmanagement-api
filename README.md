# Sistema desenvolvido para gerenciar uma API REST de um stock de camisas aonde elas são agrupadas por seus modelos
Aplicação desenvolvida durante o bootcamp realizado pela 
[Digital Innovation One](https://www.linkedin.com/school/digitalinnovation-one/) em parceria com a 
[NTT DATA](https://www.linkedin.com/company/ntt-data-europe-latam/). Durante o processo foi desenvolvido e aplicado os conceitos de 
MVC para delegar responsabilidades, pattern DTO como padrão de projeto, TDD para uma boa prática de desenvolvimento e validação das funcionalidades e o Swagger como forma de documentar a API
e apresentá-la com uma interface. E tudo isso se encontra separado por branchs.
## Objetivo ✔️
- Implementar o TDD com testes unitários para validação das funcionalidades
- Criar uma API REST satisfazendo as funcionalidades básicas de um CRUD
- Fazer uso de frameworks para agilizar o desenvolimento
- Seguir o padrão MVC
---
## Tecnologias / Bibliotecas usadas 👨🏿‍💻
- Spring Boot
- Hamcrest
- Swagger
- Postman
- Lombok
- Mockito
- JUnit 5
- Maven
- Git
- H2
---
## Objetivos futuros 💡
- [ ] Adicionar a biblioteca [MapStruct](https://mapstruct.org/) pra conversão ágil dos objetos
- [ ] Implementar um banco de dados e realizar o deploy na nuvem
---
## Baixe e execute na sua máquina
### Passo a passo:
```
# Pelo terminal, clone o repositório
$ git clone https://github.com/wpmello/stockshirtmanagement-api

# Verifique se possui o maven instalado na sua máquina
$ mvn -v
```
---
Para excutar a suíte de testes desenvolvida, digite o seguinte comando:
```
mvn clean test
```
Para executar o projeto pelo terminal, digite o seguinte comando:
```
mvn spring-boot:run
```
Após executar o comando acima, basta apenas abrir o seguinte endereço no browser e visualizar a execução do projeto:
```
http://localhost:8080/api/v1/shirts
```
Você também pode optar por utilizar a interface do Swagger para testar a API, basta digitar no browser:
```
http://localhost:8080/swagger-ui.html
```
Requisitos para rodar a aplicação:
- Java 17
- Maven 3.6.3 ou versões superiores
---
### Recomendação:
Você pode usar o [Postman](https://www.postman.com/downloads/) para testar as funcionalidades da API.
