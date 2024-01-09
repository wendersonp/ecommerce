# Sistema de processamento de ordens de ecommerce

Este pequeno sistema é dividido em dois serviços que são implementados por dois módulos. 
Um módulo, chamado receiver, recebe uma ordem de venda, realiza validações nos campos e encaminha a ordem para o outro módulo,
através de uma fila AMQP. O segundo módulo, recebe as ordens através da fila, e faz chamadas para receber a matriz de tributos,
gerar a nota fiscal e realizar a comunicação do processamento da ordem, além de armazenar o estado de todo o processo da ordem.

Para acessar a documentação, faz-se necessário executar o serviço e acessar o endpoint:

```
/swagger-ui/index.html#
```

## Requisitos

Para rodar a aplicação, é necessário ter instalado no sistema:

- Docker
- Docker-Compose
- Java 17

## Instalação

### Baixando e configurando as ferramentas necessárias

1. Com o docker propriamente instalado, baixe o arquivo [zip](https://github.com/wendersonp/ecommerce/releases)
   e salve em uma pasta de sua preferência
2. Rode o seguinte comando através do terminal no diretório com os arquivos salvos
```
docker-compose -f compose.yaml up -d
```
3. Com isto, as ferramentas já devem estar rodando normalmente

### Executando a aplicação

1. Com o arquivo [zip](https://github.com/wendersonp/ecommerce/releases) baixado, containeres criados através do docker compose,
procure pelos executaveis .jar de cada microsserviço
2. Caso não tenha feito, instale a JRE compatível com Java 17
3. Após ter executado e criado os conteineres com as ferramentas necessárias, 
ter instalado a Runtime do Java, execute a aplicação do receiver com o seguinte comando:

```
java -jar receiver-<versao>.jar
```
4. Da mesma forma, execute o microsserviço de processamento usando o seguinte comando:

```
java -jar processor-<versao>.jar
```

### Testando se tá tudo certo

Acesse o endereço a seguir no navegador para conferir se a aplicação está rodando corretamente

```
http://localhost:8080/swagger-ui/index.html
```
Caso tudo tenha dado certo, a página referida acima já ser acessada e utilizada
para gerenciar a aplicação.

A pagina em questão foi produzida com ajuda do Swagger, caso queira acessar os
endpoints da aplicação  através de outro aplicativo, obtenha a especificação
OpenAPI 3.0 da aplicação através de:

```
http://localhost:8080/v3/api-docs
```

Ou baixe a coleção do Postman pela documentação fornecida no início deste readme


### Inspecionando a fila no RabbitMQ

1. Subindo a aplicação, é possível constatar que uma _exchange_ foi criada. Porém, para receber os resultados da votação,
   é preciso configurar uma fila que ira ser ligada a exchange. Para isso, acesse o endereço (http://localhost:15672) e entre na página de gerenciamento
   do RabbitMQ, e logue com as credenciais de username: **guest**, senha: **guest**.
2. Ao executar uma requisição corretamente, uma fila também deve ser criada conectando os serviços, esta fila pode ser acessada através
   da página **queue and streams**

### Monitorando o banco de dados

A aplicação salva as ordens processadas, com ou sem erros, em um banco de dados POSTGRESQL.
Para acessa-lo, utilize a aplicação de gerenciamento de dados de sua preferência
e utilize as seguintes credenciais para acessa-lo:

- Host: localhost
- Port: 5432
- Usuario: user
- Senha: postgres
- Banco: ecommerce

### Acessando o cache

Para diminuir o número de requisições feitas ao endpoint de tributos, 
a aplicação possui um cache utilizando o banco NoSQL Redis, os caches possuem um TTL de 5 minutos.
Para acessar o cache, pode-se utilizar a aplicação Redis Insight já fornecida em um dos containeres do docker.
Para isso, acesse o link: http://localhost:8001. Utilize as seguintes credenciais para inspecionar as entradas criadas:

- Host: redis
- Port: 6379
- Name: ecommerce

Deixe os demais parâmetros com os valores padrões e não ative a validação TLS.
Va em **Browse** para verificar as entradas criadas. Lembrando que cada entrada expira em 5 minutos.
Depois disto, se faz necessário realizar a requisição novamente para criar novas entradas.


## Versão

A versão do projeto é controlada por versionamento semântico, com a criação de releases no github.

## Autores

- Wenderson, *arquiteto de software e desenvolvedor*, [wendersonp](https://github.com/wendersonp)


