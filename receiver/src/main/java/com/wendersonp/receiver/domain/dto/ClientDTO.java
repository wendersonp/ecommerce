package com.wendersonp.receiver.domain.dto;

import com.wendersonp.receiver.domain.enumeration.DocumentType;
import com.wendersonp.receiver.domain.enumeration.PersonType;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
public class ClientDTO {
    @Digits(message = "O identificador deve ser um número inteiro", integer = 20, fraction = 0)
    private String id;

    @NotEmpty(message = "O cliente deve ter um nome especificado")
    private String nome;

    @Size(min = 11, message = "O campo deve ter no mínimo 11 dígitos")
    @Digits(integer = 14, fraction = 0, message = "O documento deve ter 11 ou 14 digitos")
    private String documento;

    @NotNull
    private DocumentType tipoDocumento;

    @NotNull
    private PersonType tipoPessoa;

    @NotEmpty(message = "O endereço não pode estar vazio")
    private String endereco;

    private String numeroEndereco;

    private String complementoEndereco;

    @NotEmpty(message = "O campo não pode estar vazio")
    private String bairro;

    @NotEmpty(message = "O campo não pode estar vazio")
    private String cidade;

    @Size(min = 2, max = 2)
    @NotEmpty(message = "O campo não pode estar vazio")
    private String estado;

    @Size(min = 2, max = 2)
    @NotEmpty(message = "O campo não pode estar vazio")
    private String pais;

    @NotNull
    @Size(min = 8, max = 8)
    @Digits(integer = 8, fraction = 0, message = "O campo deve ter 8 digitos")
    private String cep;

    @Digits(integer = 10, fraction = 0)
    private String codigoIbge;

    @Pattern(regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}", message = "O telefone deve estar no formato (xx) xxxxx-xxxx")
    private String telefone;

    @Email(message = "O email deve estar em um formato valido")
    private String email;
}
