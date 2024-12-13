# ForumHUB Oracle Alura

## Descrição
O ForumHUB é uma aplicação de fórum desenvolvida como parte do curso da Alura. O objetivo é fornecer uma plataforma onde os usuários podem discutir tópicos variados, fazer perguntas e compartilhar conhecimento.

## Tecnologias Utilizadas
- Java
- Spring Boot
- Spring Data JPA
- H2 Database (para desenvolvimento)
- Swagger (para documentação da API)

## Configuração do Ambiente
### Pré-requisitos
- JDK 11 ou superior
- Maven

### Configuração do Banco de Dados
Para configurar o banco de dados, ajuste as propriedades no arquivo `application.properties` ou `application-prod.properties` conforme necessário:
- `spring.datasource.url`: URL do banco de dados
- `spring.datasource.username`: Nome de usuário do banco de dados
- `spring.datasource.password`: Senha do banco de dados

### Ativando o Perfil
Para ativar o perfil de produção, certifique-se de que a linha `spring.profiles.active=prod` esteja definida no arquivo `application.properties` ou `application-prod.properties`.

## Execução
Para executar a aplicação, utilize o seguinte comando no terminal:
```bash
mvn spring-boot:run
```

## Documentação da API
A documentação da API pode ser acessada através do Swagger em:
```
http://localhost:8080/swagger-ui.html
```

## Contribuição
Contribuições são bem-vindas! Sinta-se à vontade para abrir issues ou pull requests.

## Licença
Este projeto está licenciado sob a MIT License.
