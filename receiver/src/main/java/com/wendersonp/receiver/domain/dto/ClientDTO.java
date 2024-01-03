package com.wendersonp.receiver.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ClientDTO {
    private String id;
    private String nome;
    private String documento;
    private String tipoDocumento;
    private String tipoPessoa;
    private String endereco;
    private String numeroEndereco;
    private String complementoEndereco;
    private String bairro;
    private String cidade;
    private String estado;
    private String pais;
    private String cep;
    private String codigoIbge;
    private String telefone;
    private String email;
}
