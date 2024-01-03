# Sobre o projeto api-template

Este projeto foi concebido para avaliação técnica e validação de novas tecnologias.

**1. Domínio**

Considere e-commerce fictício que realiza vendas por vários canais de venda como site, app, loja física e caixa de autoatendimento.
Este e-commerce carece de um "orquestrador" de vendas para montar a matriz tributária dos produtos, autorizar as vendas junto a SEFAZ, 
informar a nota fiscal para o canal (e para o cliente), como também, registrar todas as vendas em seu banco de dados.

**2. Requisitos Funcionais**

**2.1. Feature Autorizar Venda (autorizar-venda.feature)**
- Caminho feliz: 
  1. Ao realizar um request válido para o endpoint "/autorizar-venda"
  2. Deveria retornar um response CREATED em processamento 
  3. Deveria postar uma mensagem com o request recebido na fila "autorizar-venda-queue"
- Fluxo excepcional:
  1. Ao realizar um request inválido (que não atende as exigências do contrato) para o endpoint "/autorizar-venda"
  2. Deveria retornar um response BAD_REQUEST com todas as respectivas mensagens de violação de contrato

**2.2. Feature Processar Autorização Venda (processar-autorizacao-venda.feature)**
- Caminho feliz:
  1. Ao consumir uma mensagem da fila "autorizar-venda-queue"
  2. Deveria obter a matriz tributária de todos os produtos através do endpoint do parceiro Tributário
  3. Deveria enviar um request com todas as informações da venda para autorização no endpoint do parceiro SEFAZ
  4. Deveria enviar um request de callback com todas as informações de confirmação da venda para o endpoint do parceiro Canal
  5. Deveria inserir no banco de dados um registro com todas as informações da venda processada
- Fluxo excepcional:
  1. Ao consumir uma mensagem da fila "autorizar-venda-queue"
  2. Caso ocorra algum erro inesperado durante as interações com algum dos parceiros (Tributário, SEFAZ ou Canal)
  3. Deveria inserir no banco de dados um registro com todas as informações da venda com erro

**3. Requisitos Não Funcionais**
- Minimizar o número de requisições "repetidas" ao endpoint do parceiro Tributário durante um intervalo de 5 minutos
- Minimizar o número de erros por perda de conexão ao realizar request para o endpoint de algum dos parceiros (Tributário, SEFAZ ou Canal)

**4. Contrato Endpoint Autorizar Venda**

- POST "/autorizar-venda"
- Request Body: 
```
{
    "canal": "APP",
    "empresa": "00001",
    "loja": "0001",
    "pdv": 501,
    "ordemPedido": {
        "numeroPedido": "101628208632",
        "numeroOrdemExterno": "100423672693-1",
        "dataAutorizacao": "2022-11-11T15:37:56.194"
    },
    "cliente": {
        "id": "123456",
        "nome": "Givaldo Santos Vasconcelos",
        "documento": "70420816097",
        "tipoDocumento": "CPF",
        "tipoPessoa": "F",
        "endereco": "Travessa Francisco Vieira",
        "numeroEndereco": "11",
        "complementoEndereco": "Apto 405",
        "bairro": "Trapiche da Barra",
        "cidade": "Maceió",
        "estado": "AL",
        "pais": "BR",
        "cep": "57010460",
        "codigoIbge": "7162435",
        "telefone": "(82) 36774-7713",
        "email": "givaldo.santos.vasconcelos@gmail.com"
    },
    "totalItens": 38744,
    "quantidadeItens": 6,
    "itens": [{
            "sku": 324226428,
            "quantidade": 3,
            "valor": 5691
        }, {
            "sku": 286441499,
            "quantidade": 2,
            "valor": 7990
        }, {
            "sku": 183675297,
            "quantidade": 1,
            "valor": 5691
        }
    ]
}

OBS: Onde os campos totalItens e valor são multiplicados por 100
```
- Response Body:
```
{
    "status": "EM_PROCESSAMENTO",
    "dataResposta": "2022-11-11T15:37:59.194"
}
```

**5. Parceiros**

**5.1. Tributário**

- GET "/tributo?sku=324226428"
- Response Body:
```
{
    "sku": 324226428,
    "valorIcms": 38,
    "valorPis": 12,
    "valorDifaul": 9,
    "valorFcpIcms": 58
}
```

**5.2. SEFAZ**

- POST "/authorize"
- Request Body:
```
{
    "orderNumber": "101628208632",
    "externalOrderNumber": "100423672693-1",
    "customer": {
        "id": "123456",
        "name": "Givaldo Santos Vasconcelos",
        "document": "70420816097",
        "documentType": "CPF",
        "personType": "F",
        "address": "Travessa Francisco Vieira",
        "addressNumber": "11",
        "addressComplement": "Apto 405",
        "district": "Trapiche da Barra",
        "city": "Maceió",
        "state": "AL",
        "country": "BR",
        "zipCode": "57010460",
        "ibgeCode": "7162435",
        "phoneNumber": "(82) 36774-7713",
        "email": "givaldo.santos.vasconcelos@gmail.com"
    },
    "products": [{
            "sku": 324226428,
            "amount": 3,
            "value": 56.91,
            "icmsValue": 38,
            "pisValue": 12,
            "difaulValue": 9,
            "fcpIcmsValue": 58
        }, {
            "sku": 255227523,
            "amount": 2,
            "value": 79.90,
            "icmsValue": 29,
            "pisValue": 53,
            "difaulValue": 90,
            "fcpIcmsValue": 49
        }, {
            "sku": 333020086,
            "amount": 1,
            "value": 56.91,
            "icmsValue": 82,
            "pisValue": 58,
            "difaulValue": 69,
            "fcpIcmsValue": 24
        }
    ]
}
```

- Response Body:
```
{
    "nfeKey": "43210392754738001134550040000159551330237069",
    "invoiceNumber": "0237069",
    "issuanceDate": "2022-11-11T15:38:00.012",
    "invoice": "NDMyMTAzOTI3NTQ3MzgwMDExMzQ1NTAwNDAwMDAxNTk1NTEzMzAyMzcwNjk="
}
```

**5.3. Canal**

- POST "/callback-venda"
- Request Body:
```
{
    "numeroPedido": "101628208632",
    "numeroOrdemExterno": "100423672693-1",
    "chaveNFE": "43210392754738001134550040000159551330237069",
    "numeroNota": "0237069",
    "dataEmissao": "2022-11-11T15:38:00.012",
    "pdf": "NDMyMTAzOTI3NTQ3MzgwMDExMzQ1NTAwNDAwMDAxNTk1NTEzMzAyMzcwNjk=",
    "status": "PROCESSADO"
}
```
- Response Body:
```
Venda 100423672693-1 recebida com sucesso
```

**6. Banco de Dados**

- Tabela:
```
CREATE TABLE ECOMMERCE.VENDA
(
    ID                       NUMBER(38, 0) NOT NULL,
    CANAL                    VARCHAR2(100) NOT NULL,
    CODIGO_EMPRESA           INTEGER       NOT NULL,
    CODIGO_LOJA              INTEGER       NOT NULL,
    NUMERO_PDV               INTEGER       NOT NULL,
    NUMERO_PEDIDO            VARCHAR2(38)  NOT NULL,
    NUMERO_ORDEM_EXTERNO     VARCHAR2(38)  NOT NULL,
    VALOR_TOTAL              NUMBER(38, 2) NOT NULL,
    QTD_ITEM                 NUMBER(38,0)  NOT NULL,
    VENDA_REQUEST            CLOB          NOT NULL,
    DATA_ATUALIZACAO         TIMESTAMP     NOT NULL,
    DATA_REQUISICAO          TIMESTAMP     NOT NULL,
    CHAVE_NFE                VARCHAR2(44),
    NUMERO_NOTA              NUMBER(38,0),
    DATA_EMISSAO             TIMESTAMP,
    PDF                      CLOB,
    SITUACAO                 VARCHAR2(100) NOT NULL,
    MOTIVO                   VARCHAR2(255),
    CONSTRAINT PK_DEVOLUCAO_PEDIDO PRIMARY KEY (ID)
)
TABLESPACE ECOMMERCE_DAT;
```
- Sequence:
```
CREATE SEQUENCE ECOMMERCE.VENDA_SEQ MINVALUE 1 START WITH 1 INCREMENT BY 1 CACHE 20 NOORDER NOCYCLE;
```

**7. Requisitos Técnicos**
- Linguagem Java 11 ou superior
- Ecossistema do Spring Boot 2.4.0 ou superior
- Mensageria com RabbitMQ ou Kafka
- Banco de dados com H2, Oracle ou Postgres
- Testes com JUnit, Mockito e/ou Cucumber
- Documentação da API com Swagger

**8. Sobre os critérios de avaliação**
1. O mínimo que se espera do candidato é que seja implementado os requisítos funcionais de acordo com os requisitos técnicos.
2. Serão avaliadas as habilidades do candidato de acordo com as boas práticas de desenvolvimento de software, orientação a objetos, código limpo e SOLID.
3. Será avaliado a sinergia do candidato com os frameworks de teste, assim como, os níveis de cobertura de código.
4. Será visto como um grande diferencial se o candidato implementar testes de aceitação (acceptance test ou end-to-end test) utilizando Cucumber.

**9. That's all folks!**

Boa sorte ao candidato.