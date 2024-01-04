package com.wendersonp.processor.infrastructure.client.sefaz.dto;

import com.wendersonp.receiver.domain.dto.ClientDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
public class SefazRequestClientDTO {
    private String id;
    private String name;
    private String document;
    private String documentType;
    private String personType;
    private String address;
    private String addressNumber;
    private String addressComplement;
    private String district;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private String ibgeCode;
    private String phoneNumber;
    private String email;

    public SefazRequestClientDTO(ClientDTO client) {
        this.id = client.getId();
        this.name = client.getNome();
        this.document = client.getDocumento();
        this.documentType = client.getTipoDocumento().toString();
        this.personType = client.getTipoPessoa().toString();
        this.address = client.getEndereco();
        this.addressNumber = client.getNumeroEndereco();
        this.addressComplement = client.getComplementoEndereco();
        this.district = client.getBairro();
        this.city = client.getCidade();
        this.state = client.getEstado();
        this.country = client.getPais();
        this.zipCode = client.getCep();
        this.ibgeCode = client.getCodigoIbge();
        this.phoneNumber = client.getTelefone();
        this.email = client.getEmail();
    }
}
