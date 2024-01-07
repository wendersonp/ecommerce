package com.wendersonp.receiver.cucumber.steps;

import com.wendersonp.receiver.cucumber.util.LocalDateTimeWrapper;
import com.wendersonp.receiver.cucumber.util.RestClient;
import com.wendersonp.receiver.domain.dto.OrderDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.http.HttpStatus;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
public class ValidationSteps {

    @Autowired
    private LocalDateTimeWrapper dateTimeWrapper;

    @Autowired
    private OrderDTO order;

    @Autowired
    private OutputDestination outputDestination;


    private MockMvcResponse response;


    @Quando("autorizar venda")
    public void quandoAutorizarVenda() {
        response = RestClient.getRequestSpecification()
                .body(order)
                .when()
                .post("/autorizar-venda?date=" +
                        dateTimeWrapper.getDateTime().format(dateTimeWrapper.getFormatter()));
    }

    @Entao("deveria receber os dados de venda response")
    public void entaoDeveriaReceberDadosVendaResponse(DataTable dataTable) {
        Map<String, String> data = dataTable
                .transpose()
                .asMap(String.class, String.class);

        response.then()
                .body("status", Matchers.equalTo(data.get("Status")))
                .body("dataResposta", Matchers.equalTo(data.get("Data Resposta")));
    }

    @Entao("deveria publicar o Json Venda Request na fila {string}")
    public void deveria_publicar_o_json_venda_request_na_fila(String fila) {
        var message = outputDestination.receive(2, fila + "-out-0").getPayload();
        assertThat(message, notNullValue());
    }

    @Entao("deveria retornar as seguintes mensagens")
    public void deveria_retornar_as_seguintes_mensagens(DataTable dataTable) {
        Map<String, String> data = dataTable.transpose().asMap(String.class, String.class);
        response.then()
                .status(HttpStatus.valueOf(data.get("Http Status")));
        var responseBody = response.getBody();
        Map<String, String> errors = responseBody
                .path("errors");

        List<String> actualErrorsList = new ArrayList<>();
        errors.forEach((key, value) -> {
            var errorSplit = value.split(",\\s(?!123)");
            for (var error: errorSplit) {
                actualErrorsList.add(key + " " + error);
            }
        });


        List<String> expectedErrorsList = Arrays.asList(data
                .get("Message")
                .substring(0, data.get("Message").length() - 1)
                .split(",\\s(?!123)")
        );

        assertThat(actualErrorsList, containsInAnyOrder(expectedErrorsList.toArray()));
    }



    @Entao("nao deveria publicar nenhum JSON na fila")
    public void nao_deveria_publicar_nenhum_json_na_fila(DataTable dataTable) {
        Map<String, String> data = dataTable.transpose().asMap(String.class, String.class);
        var message = outputDestination.receive(2, data.get("Nome Fila") + "-out-0");
        assertThat(message, is(nullValue()));
    }

}
