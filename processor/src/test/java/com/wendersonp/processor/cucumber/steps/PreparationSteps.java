package com.wendersonp.processor.cucumber.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wendersonp.processor.cucumber.util.LocalDateTimeWrapper;
import com.wendersonp.processor.domain.dto.ItemFeesDTO;
import com.wendersonp.processor.domain.enumeration.OrderStatusEnum;
import com.wendersonp.processor.domain.models.SaleOrderEntity;
import com.wendersonp.processor.domain.repository.SaleOrderRepository;
import com.wendersonp.receiver.domain.dto.ClientDTO;
import com.wendersonp.receiver.domain.dto.ItemDTO;
import com.wendersonp.receiver.domain.dto.OrderDTO;
import com.wendersonp.receiver.domain.dto.OrderInfoDTO;
import com.wendersonp.receiver.domain.enumeration.DocumentType;
import com.wendersonp.receiver.domain.enumeration.PersonType;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

import static com.wendersonp.processor.cucumber.util.Util.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.BDDMockito.*;

@Slf4j
public class PreparationSteps {

    @Autowired
    private OrderDTO order;

    @Autowired
    private SaleOrderEntity initialEntity;

    @Autowired
    private LocalDateTimeWrapper dateTimeWrapper;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private SaleOrderRepository repository;

    @Autowired
    private Clock clock;

    private Clock fixedClock;

    @Dado("que o sistema seja executado na seguinte data e hora")
    public void dadoSistemaExecutadoDataHora(DataTable dataTable) {
        Map<String, Integer> data = dataTable.transpose().asMap(String.class, Integer.class);

        dateTimeWrapper.setDateTime(
                LocalDateTime.of(
                        data.get("Ano"),
                        data.get("Mes"),
                        data.get("Dia"),
                        data.get("Hora"),
                        data.get("Minuto"),
                        data.get("Segundo")
                )
        );
        fixedClock = Clock.fixed(
                dateTimeWrapper.getDateTime().toInstant(ZoneOffset.UTC),
                ZoneId.of("UTC")
        );

        given(clock.getZone()).willReturn(fixedClock.getZone());
        given(clock.instant()).willReturn(fixedClock.instant());
        given(clock.withZone(any())).willReturn(fixedClock.withZone(ZoneId.of("UTC")));
    }

    @Dado("que seja informado os dados de venda request")
    public void dadoInformadoDadosVendaRequest(DataTable dataTable) {
        Map<String, String> data = dataTable.transpose().asMap(String.class, String.class);

        order.setCanal(data.get("Canal"));
        order.setEmpresa(data.get("Empresa"));
        order.setLoja(data.get("Loja"));
        order.setPdv(castIfNotNull(data.get("Pdv"), Integer::parseInt));
        order.setTotalItens(castIfNotNull(data.get("Total Itens"), BigInteger::new));
        order.setQuantidadeItens(castIfNotNull(data.get("Quantidade Itens"), BigInteger::new));
    }

    @E("que seja informado os dados de ordem pedido")
    public void eInformadoDadosOrdemPedido(DataTable dataTable) {
        Map<String, String> data = dataTable.transpose().asMap(String.class, String.class);

        OrderInfoDTO info = new OrderInfoDTO(
                data.get("Numero Pedido"),
                data.get("Numero Ordem Externo"),
                castIfNotNull(data.get("Data Autorizacao"), date -> LocalDateTime.parse(data.get("Data Autorizacao"), formatterWithMillis))
        );
        order.setOrdemPedido(info);
    }

    @E("que seja informado os dados de cliente")
    public void eInformadoDadosCliente(DataTable dataTable) {
        Map<String, String> data = dataTable.transpose().asMap(String.class, String.class);

        ClientDTO client = ClientDTO.builder()
                .id(data.get("Id"))
                .nome(data.get("Nome"))
                .documento(data.get("Documento"))
                .tipoDocumento(castIfNotNull(data.get("Tipo Documento"), DocumentType::valueOf))
                .tipoPessoa(castIfNotNull(data.get("Tipo Pessoa"), PersonType::valueOf))
                .endereco(data.get("Endereco"))
                .numeroEndereco(data.get("Numero Endereco"))
                .complementoEndereco(data.get("Complemento Endereco"))
                .bairro(data.get("Bairro"))
                .cidade(data.get("Cidade"))
                .estado(data.get("Estado"))
                .pais(data.get("Pais"))
                .cep(data.get("Cep"))
                .codigoIbge(data.get("Codigo Ibge"))
                .telefone(data.get("Telefone"))
                .email(data.get("Email"))
                .build();
        order.setCliente(client);
    }

    @E("que seja informado os dados dos itens")
    public void eInformadoDadosItens(DataTable dataTable) {
        List<Map<String, Integer>> data = dataTable.asMaps(String.class, Integer.class);

        List<ItemDTO> items = data.stream().map(element ->
                new ItemDTO(
                        element.get("Sku"),
                        element.get("Quantidade"),
                        element.get("Valor")
                )
        ).toList();
        order.setItens(items);
    }

    @E("que existam os seguintes tributo response cadastrados externamente")
    public void eExistamOsTributoResponseCadastrados(DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);

        var responses = data
                .stream()
                .map(this::toItemFees)
                .toList();


        responses.forEach(response ->
            stubFor(
                    get(urlEqualTo("/tributo?sku=" + response.getSku()))
                            .willReturn(aResponse()
                                    .withStatus(HttpStatus.OK.value())
                                    .withBody(toByteArray(response))
                                    .withHeader("Content-Type", "application/json")
                            )

            )
        );
    }

    @E("que existam os seguintes authorize response cadastrados externamente")
    public void eExistamOsSeguintesAuthorizeResponseCadastrados(DataTable dataTable) {
        Map<String, String> response = dataTable.transpose().asMap(String.class, String.class);

        response = formatMapToResponseBody(response);

        stubFor(
                post(urlEqualTo("/authorize"))
                        .willReturn(aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader("Content-Type", "application/json")
                                .withBody(toByteArray(response))
                        )
        );
    }

    @Dado("que existam os seguintes canal response cadastrados externamente")
    public void dadoExistamSeguintesCanalResponseCadastrados(DataTable dataTable) {
        Map<String, String> data = dataTable.transpose().asMap(String.class, String.class);

        var response = formatMapToResponseBody(data);

        HttpStatus status;

        if (response.get("mensagem").contains("erro")) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            status = HttpStatus.OK;
        }

        stubFor(
                post(urlEqualTo("/callback-venda"))
                        .willReturn(aResponse()
                                .withStatus(status.value())
                                .withHeader("Content-Type", "application/json")
                                .withBody(toByteArray(response))
                        )
        );
    }

    @E("que existam as vendas cadastradas")
    public void eQueExistamAsVendasCadastradas(DataTable dataTable) {
        Map<String, String> data = dataTable.transpose().asMap(String.class, String.class);

        SaleOrderEntity entity = buildEntity(data);

        repository.save(entity);
    }

    @SneakyThrows
    private SaleOrderEntity buildEntity(Map<String, String> data) {
        return SaleOrderEntity.builder()
                .canal(data.get("Canal"))
                .codigoEmpresa(Integer.parseInt(data.get("Codigo Empresa")))
                .codigoLoja(Integer.parseInt(data.get("Codigo Loja")))
                .numeroPdv(Integer.parseInt(data.get("Numero Pdv")))
                .numeroPedido(data.get("Numero Pedido"))
                .numeroOrdemExterno(data.get("Numero Ordem Externo"))
                .valorTotal(new BigDecimal(data.get("Valor Total")))
                .qtdItem(new BigInteger(data.get("Quantidade Itens")))
                .vendaRequest(toJsonString(order))
                .dataAtualizacao(LocalDateTime.parse(data.get("Data Atualizacao"), formatterWithoutMillis))
                .dataRequisicao(LocalDateTime.parse(data.get("Data Requisicao"), formatterWithMillis))
                .chaveNfe(data.get("Chave NFE"))
                .numeroNota(castIfNotNull(data.get("Numero Nota"), BigInteger::new))
                .dataEmissao(castIfNotNull(data.get("Data Emissao"), date -> LocalDateTime.parse(date, formatterWithMillis)))
                .pdf(data.get("Pdf"))
                .situacao(OrderStatusEnum.valueOf(data.get("Situacao")))
                .motivo(data.get("Motivo"))
                .build();
    }

    @NotNull
    private ItemFeesDTO toItemFees(Map<String, String> element) {
        return ItemFeesDTO.builder()
                .sku(castIfNotNull(element.get("Sku"), Integer::parseInt))
                .valorIcms(castIfNotNull(element.get("Valor Icms"), (value) -> (int) Double.parseDouble(value)))
                .valorDifaul(castIfNotNull(element.get("Valor Difaul"), (value) -> (int) Double.parseDouble(value)))
                .valorPis(castIfNotNull(element.get("Valor Pis"), (value) -> (int) Double.parseDouble(value)))
                .valorFcpIcms(castIfNotNull(element.get("Valor Fcp Icms"), (value) -> (int) Double.parseDouble(value)))
                .build();
    }

    @SneakyThrows
    private <T> byte[] toByteArray(T object) {
        return mapper.writeValueAsBytes(object);
    }

    @SneakyThrows
    public <T> String toJsonString(T object) {
        return mapper.writeValueAsString(object);
    }

}
