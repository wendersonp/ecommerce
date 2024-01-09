package com.wendersonp.receiver.cucumber.steps;

import com.wendersonp.receiver.cucumber.util.LocalDateTimeWrapper;
import com.wendersonp.receiver.domain.dto.ClientDTO;
import com.wendersonp.receiver.domain.dto.ItemDTO;
import com.wendersonp.receiver.domain.dto.OrderDTO;
import com.wendersonp.receiver.domain.dto.OrderInfoDTO;
import com.wendersonp.receiver.domain.enumeration.DocumentType;
import com.wendersonp.receiver.domain.enumeration.PersonType;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static com.wendersonp.receiver.cucumber.util.Util.castIfNotNull;

@Slf4j
public class PreparationSteps {

    @Autowired
    private LocalDateTimeWrapper dateTimeWrapper;

    @Autowired
    private OrderDTO order;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");


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
                castIfNotNull(data.get("Data Autorizacao"), date -> LocalDateTime.parse(data.get("Data Autorizacao"), formatter))
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

}
