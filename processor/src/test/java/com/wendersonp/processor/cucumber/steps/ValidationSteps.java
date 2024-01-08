package com.wendersonp.processor.cucumber.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wendersonp.processor.cucumber.util.Util;
import com.wendersonp.processor.domain.dto.InvoiceDTO;
import com.wendersonp.processor.domain.models.SaleOrderEntity;
import com.wendersonp.processor.domain.repository.SaleOrderRepository;
import com.wendersonp.processor.infrastructure.client.channel.CallbackChannelClient;
import com.wendersonp.processor.infrastructure.client.sefaz.SefazClient;
import com.wendersonp.processor.infrastructure.client.sefaz.dto.SefazRequestClientDTO;
import com.wendersonp.processor.infrastructure.client.sefaz.dto.SefazRequestItemDTO;
import com.wendersonp.processor.infrastructure.client.sefaz.dto.SefazRequestOrderDTO;
import com.wendersonp.receiver.domain.dto.OrderDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static com.wendersonp.processor.cucumber.util.Util.castIfNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static com.wendersonp.processor.cucumber.util.Util.*;

@Slf4j
public class ValidationSteps {

    @Autowired
    private InputDestination destination;

    @Autowired
    private OrderDTO order;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private SaleOrderRepository repository;

    @Autowired
    private SefazClient sefazClient;

    @Autowired
    private CallbackChannelClient callbackClient;

    private final ArgumentCaptor<SefazRequestOrderDTO> authorizeCaptor = ArgumentCaptor.forClass(SefazRequestOrderDTO.class);

    private final ArgumentCaptor<InvoiceDTO> invoiceCaptor = ArgumentCaptor.forClass(InvoiceDTO.class);

    @Quando("processar autorizacao venda")
    public void quandoProcessarAutorizacaoVenda() {
        Message<OrderDTO> message = MessageBuilder.withPayload(order).build();
        destination.send(message, "autorizarVendaQueue-in-0");
    }

    @Entao("deveria existir as seguintes vendas na base")
    public void entaoDeveriaExistirVendasNaBase(DataTable dataTable) {
        List<Map<String, String>> expected = dataTable.asMaps(String.class, String.class);

        List<SaleOrderEntity> entities = repository.findAll();
        assertThat(entities.size(), equalTo(expected.size()));

        expected.forEach(expectedEntry -> {
            var actualEntityOptional = repository.findById(new BigInteger(expectedEntry.get("Id")));
            assertThat(actualEntityOptional.isEmpty(), equalTo(false));
            var actualEntity = actualEntityOptional.get();

            assertEntity(expectedEntry, actualEntity);
        });
    }

    @E("deveria enviar para o endpoint authorize as informacoes de request esperadas")
    public void eDeveriaEnviarParaAuthorizeInfoRequestEsperadas(DataTable dataTable) {
        verify(sefazClient, times(1)).authorizeOrder(authorizeCaptor.capture());

        var actual = authorizeCaptor.getValue();
        var expectedMap = formatKeysToCamelCase(
                dataTable
                        .transpose()
                        .asMap(String.class, Object.class)
        );
        var expected = mapper.convertValue(expectedMap, SefazRequestOrderDTO.class);

        assertThat(actual.getOrderNumber(), equalTo(expected.getOrderNumber()));
        assertThat(actual.getExternalOrderNumber(), equalTo(expected.getExternalOrderNumber()));
    }

    @E("deveria enviar para o endpoint authorize as informacoes de customer esperadas")
    public void eDeveriaEnviarParaAuthorizeInfoCustomerEsperadas(DataTable dataTable) {
        var actual = authorizeCaptor.getValue().getCustomer();
        var expectedMap = formatKeysToCamelCase(
                dataTable
                        .transpose()
                        .asMap(String.class, Object.class)
        );
        var expected = mapper.convertValue(expectedMap, SefazRequestClientDTO.class);

        assertThat(actual, equalTo(expected));
    }

    @E("deveria enviar para o endpoint authorize as informacoes de product esperadas")
    public void eDeveriaEnviarParaAuthorizeInfoProductEsperadas(DataTable dataTable) {
        var actual = authorizeCaptor.getValue().getProducts();

        List<Map<String, Object>> expectedData = dataTable
                .asMaps(String.class, Object.class);
        var expected = expectedData
                .stream()
                .map(Util::formatKeysToCamelCase)
                .map(expectedElement ->
                        mapper.convertValue(expectedElement, SefazRequestItemDTO.class)
                ).toList();

        assertThat(actual, containsInAnyOrder(expected.toArray()));
    }

    @E("deveria enviar para o endpoint callback-venda as informacoes de request esperadas")
    public void eDeveriaEnviarParaOEndpointCallbackVendaInfoRequestEsperadas(DataTable dataTable) {
        verify(callbackClient, times(1)).communicateInvoice(invoiceCaptor.capture());

        var actual = invoiceCaptor.getValue();
        var expectedMap = formatKeysToCamelCase(
                dataTable
                        .transpose()
                        .asMap(String.class, Object.class)
        );
        var expected = mapper.convertValue(expectedMap, InvoiceDTO.class);

        assertThat(actual, equalTo(expected));
    }

    private void assertEntity(Map<String, String> expectedEntry, SaleOrderEntity actualEntity) {
        assertThat(
                actualEntity.getCanal(),
                equalTo(expectedEntry.get("Canal"))
        );
        assertThat(
                actualEntity.getCodigoEmpresa(),
                equalTo(Integer.parseInt(expectedEntry.get("Codigo Empresa")))
        );
        assertThat(
                actualEntity.getCodigoLoja(),
                equalTo(Integer.parseInt(expectedEntry.get("Codigo Loja")))
        );
        assertThat(
                actualEntity.getNumeroPdv(),
                equalTo(Integer.parseInt(expectedEntry.get("Numero Pdv")))
        );
        assertThat(
                actualEntity.getNumeroPedido(),
                equalTo(expectedEntry.get("Numero Pedido"))
        );
        assertThat(
                actualEntity.getNumeroOrdemExterno(),
                equalTo(expectedEntry.get("Numero Ordem Externo"))
        );
        assertThat(
                actualEntity.getValorTotal(),
                equalTo(new BigDecimal(expectedEntry.get("Valor Total")))
        );
        assertThat(
                actualEntity.getQtdItem().toString(),
                equalTo(expectedEntry.get("Quantidade Itens"))
        );
        assertThat(
                actualEntity.getVendaRequest(),
                equalTo(toJsonString(order))
        );
        assertThat(
                actualEntity.getDataAtualizacao().format(formatterWithoutMillis),
                equalTo(expectedEntry.get("Data Atualizacao"))
        );
        assertThat(
                actualEntity.getDataRequisicao().format(formatterWithMillis),
                equalTo(expectedEntry.get("Data Requisicao"))
        );
        assertThat(
                castIfNotNull(actualEntity.getChaveNfe(), String::valueOf),
                equalTo(expectedEntry.get("Chave NFE"))
        );
        assertThat(
                actualEntity.getNumeroNota(),
                equalTo(castIfNotNull(expectedEntry.get("Numero Nota"), BigInteger::new))
        );
        assertThat(
                castIfNotNull(actualEntity.getDataEmissao(),
                        date -> date.format(formatterWithMillis)),
                equalTo(expectedEntry.get("Data Emissao"))
        );
        assertThat(
                actualEntity.getPdf(),
                equalTo(expectedEntry.get("Pdf"))
        );
        assertThat(
                actualEntity.getSituacao().toString(),
                equalTo(expectedEntry.get("Situacao"))
        );
        if (expectedEntry.get("Motivo") != null) {
            assertThat(
                    actualEntity.getMotivo(),
                    containsString(expectedEntry.get("Motivo"))
            );
        }
    }

    @SneakyThrows
    private String toJsonString(Object object) {
        return mapper.writeValueAsString(object);
    }
}
