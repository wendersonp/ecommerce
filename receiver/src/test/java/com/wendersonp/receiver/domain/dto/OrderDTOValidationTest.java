package com.wendersonp.receiver.domain.dto;

import com.wendersonp.receiver.domain.dto.fixture.Fixture;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OrderDTOValidationTest {

    private final PodamFactory factory = new PodamFactoryImpl();

    private final OrderDTO validOrderDTO = new Fixture<OrderDTO>().fromFile("static/valid_order.json", OrderDTO.class);
    private OrderDTO invalidOrderDTO;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @BeforeEach
    void setUp() {
        invalidOrderDTO = factory.manufacturePojo(OrderDTO.class);
    }

    @Test
    void validOrderDTOTest() {
        var constraints = validator.validate(validOrderDTO);
        assertTrue(constraints.isEmpty());
    }

    @Test
    void invalidOrderClientIdTest() {
        invalidOrderDTO.getCliente().setId("ABC");
        var constraints = validator.validate(invalidOrderDTO);
        List<String> invalidFields = constraints
                .stream()
                .map(constraint -> constraint.getPropertyPath().toString())
                .toList();
        assertTrue(invalidFields.contains("cliente.id"));
    }

    @Test
    void invalidOrderClientNameTest() {
        invalidOrderDTO.getCliente().setNome("");
        var constraints = validator.validate(invalidOrderDTO);
        List<String> invalidFields = constraints
                .stream()
                .map(constraint -> constraint.getPropertyPath().toString())
                .toList();
        assertTrue(invalidFields.contains("cliente.nome"));
    }

    @Test
    void invalidOrderClientDocumentTest() {
        invalidOrderDTO.getCliente().setDocumento("AAAAAAA");
        var constraints = validator.validate(invalidOrderDTO);
        List<String> invalidFields = constraints
                .stream()
                .map(constraint -> constraint.getPropertyPath().toString())
                .toList();
        assertTrue(invalidFields.contains("cliente.documento"));
    }

    @Test
    void invalidOrderDocumentTypeTest() {
        invalidOrderDTO.getCliente().setTipoDocumento(null);
        var constraints = validator.validate(invalidOrderDTO);
        List<String> invalidFields = constraints
                .stream()
                .map(constraint -> constraint.getPropertyPath().toString())
                .toList();
        assertTrue(invalidFields.contains("cliente.tipoDocumento"));
    }

    @Test
    void invalidOrderPersonTypeTest() {
        invalidOrderDTO.getCliente().setTipoPessoa(null);
        var constraints = validator.validate(invalidOrderDTO);
        List<String> invalidFields = constraints
                .stream()
                .map(constraint -> constraint.getPropertyPath().toString())
                .toList();
        assertTrue(invalidFields.contains("cliente.tipoPessoa"));
    }

    @Test
    void invalidOrderClientAddressTest() {
        invalidOrderDTO.getCliente().setEndereco("");
        var constraints = validator.validate(invalidOrderDTO);
        List<String> invalidFields = constraints
                .stream()
                .map(constraint -> constraint.getPropertyPath().toString())
                .toList();
        assertTrue(invalidFields.contains("cliente.endereco"));
    }

    @Test
    void invalidOrderClientDistrictTest() {
        invalidOrderDTO.getCliente().setBairro("");
        var constraints = validator.validate(invalidOrderDTO);
        List<String> invalidFields = constraints
                .stream()
                .map(constraint -> constraint.getPropertyPath().toString())
                .toList();
        assertTrue(invalidFields.contains("cliente.bairro"));
    }

    @Test
    void invalidOrderClientCityTest() {
        invalidOrderDTO.getCliente().setCidade("");
        var constraints = validator.validate(invalidOrderDTO);
        List<String> invalidFields = constraints
                .stream()
                .map(constraint -> constraint.getPropertyPath().toString())
                .toList();
        assertTrue(invalidFields.contains("cliente.cidade"));
    }

    @Test
    void invalidOrderClientStateTest() {
        invalidOrderDTO.getCliente().setEstado("PAP");
        var constraints = validator.validate(invalidOrderDTO);
        List<String> invalidFields = constraints
                .stream()
                .map(constraint -> constraint.getPropertyPath().toString())
                .toList();
        assertTrue(invalidFields.contains("cliente.estado"));
    }

    @Test
    void invalidOrderClientCountryTest() {
        invalidOrderDTO.getCliente().setPais("PAP");
        var constraints = validator.validate(invalidOrderDTO);
        List<String> invalidFields = constraints
                .stream()
                .map(constraint -> constraint.getPropertyPath().toString())
                .toList();
        assertTrue(invalidFields.contains("cliente.pais"));
    }

    @Test
    void invalidOrderClientCepTest() {
        invalidOrderDTO.getCliente().setCep("44444");
        var constraints = validator.validate(invalidOrderDTO);
        List<String> invalidFields = constraints
                .stream()
                .map(constraint -> constraint.getPropertyPath().toString())
                .toList();
        assertTrue(invalidFields.contains("cliente.cep"));
    }

    @Test
    void invalidOrderClientPhoneTest() {
        invalidOrderDTO.getCliente().setTelefone("(88) AAA-3322");
        var constraints = validator.validate(invalidOrderDTO);
        List<String> invalidFields = constraints
                .stream()
                .map(constraint -> constraint.getPropertyPath().toString())
                .toList();
        assertTrue(invalidFields.contains("cliente.telefone"));
    }

    @Test
    void invalidOrderClientEmailTest() {
        invalidOrderDTO.getCliente().setEmail("testeNoEmail");
        var constraints = validator.validate(invalidOrderDTO);
        List<String> invalidFields = constraints
                .stream()
                .map(constraint -> constraint.getPropertyPath().toString())
                .toList();
        assertTrue(invalidFields.contains("cliente.email"));
    }

    @Test
    void invalidOrderItemTest() {
        List<ItemDTO> items = List.of(
                new ItemDTO(-3, -4, 0)
        );
        invalidOrderDTO.setItens(items);
        var constraints = validator.validate(invalidOrderDTO);
        List<String> invalidFields = constraints
                .stream()
                .map(constraint -> constraint.getPropertyPath().toString())
                .toList();
        assertTrue(
                invalidFields.containsAll(
                Set.of(
                        "itens[0].sku",
                        "itens[0].quantidade",
                        "itens[0].valor"
                )
        ));
    }

    @Test
    void invalidOrderInfoTest() {
        invalidOrderDTO.setOrdemPedido(new OrderInfoDTO(
                "1231231231231231231223123",
                "",
                LocalDateTime.now()
        ));
        var constraints = validator.validate(invalidOrderDTO);
        List<String> invalidFields = constraints
                .stream()
                .map(constraint -> constraint.getPropertyPath().toString())
                .toList();
        assertTrue(
                invalidFields.containsAll(
                        Set.of(
                                "ordemPedido.numeroPedido",
                                "ordemPedido.numeroOrdemExterno"
                        )
                ));
    }

    @Test
    void invalidOrderChannelTest() {
        invalidOrderDTO.setCanal("");
        var constraints = validator.validate(invalidOrderDTO);
        List<String> invalidFields = constraints
                .stream()
                .map(constraint -> constraint.getPropertyPath().toString())
                .toList();
        assertTrue(invalidFields.contains("canal"));
    }

    @Test
    void invalidOrderCompanyTest() {
        invalidOrderDTO.setEmpresa("333334");
        var constraints = validator.validate(invalidOrderDTO);
        List<String> invalidFields = constraints
                .stream()
                .map(constraint -> constraint.getPropertyPath().toString())
                .toList();
        assertTrue(invalidFields.contains("empresa"));
    }

    @Test
    void invalidOrderStoreTest() {
        invalidOrderDTO.setLoja("333334");
        var constraints = validator.validate(invalidOrderDTO);
        List<String> invalidFields = constraints
                .stream()
                .map(constraint -> constraint.getPropertyPath().toString())
                .toList();
        assertTrue(invalidFields.contains("loja"));
    }

    @Test
    void invalidOrderPdvTest() {
        invalidOrderDTO.setPdv(-333);
        var constraints = validator.validate(invalidOrderDTO);
        List<String> invalidFields = constraints
                .stream()
                .map(constraint -> constraint.getPropertyPath().toString())
                .toList();
        assertTrue(invalidFields.contains("pdv"));
    }

    @Test
    void invalidOrderTotalItemsTest() {
        invalidOrderDTO.setTotalItens(BigInteger.ZERO);
        var constraints = validator.validate(invalidOrderDTO);
        List<String> invalidFields = constraints
                .stream()
                .map(constraint -> constraint.getPropertyPath().toString())
                .toList();
        assertTrue(invalidFields.contains("totalItens"));
    }

    @Test
    void invalidOrderItemsQuantityTest() {
        invalidOrderDTO.setQuantidadeItens(BigInteger.ZERO);
        var constraints = validator.validate(invalidOrderDTO);
        List<String> invalidFields = constraints
                .stream()
                .map(constraint -> constraint.getPropertyPath().toString())
                .toList();
        assertTrue(invalidFields.contains("quantidadeItens"));
    }


}