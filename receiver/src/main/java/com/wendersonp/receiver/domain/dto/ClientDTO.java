package com.wendersonp.receiver.domain.dto;

import com.wendersonp.receiver.domain.enumeration.DocumentType;
import com.wendersonp.receiver.domain.enumeration.PersonType;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.wendersonp.receiver.domain.util.ValidationMessages.*;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDTO {
    @NotBlank(message = NOT_BLANK)
    @Digits(message = INTEGER_FORMAT, integer = 20, fraction = 0)
    private String id;

    @NotBlank(message = NOT_BLANK)
    private String nome;

    @NotBlank(message = NOT_BLANK)
    @Size(min = 11, message = ELEVEN_DIGITS)
    @Digits(integer = 14, fraction = 0, message = ELEVEN_FOURTEEN_DIGITS)
    private String documento;

    @NotNull(message = NOT_BLANK)
    private DocumentType tipoDocumento;

    @NotNull(message = NOT_NULL)
    private PersonType tipoPessoa;

    @NotBlank(message = NOT_BLANK)
    private String endereco;

    @NotBlank(message = NOT_BLANK)
    private String numeroEndereco;

    private String complementoEndereco;

    @NotBlank(message = NOT_BLANK)
    private String bairro;

    @NotBlank(message = NOT_BLANK)
    private String cidade;

    @Size(min = 2, max = 2)
    @NotBlank(message = NOT_BLANK)
    private String estado;

    @Size(min = 2, max = 2)
    @NotBlank(message = NOT_BLANK)
    private String pais;

    @NotBlank(message = NOT_BLANK)
    @Size(min = 8, max = 8)
    @Digits(integer = 8, fraction = 0, message = EIGHT_DIGITS)
    private String cep;

    @NotBlank(message = NOT_BLANK)
    @Digits(integer = 10, fraction = 0)
    private String codigoIbge;

    @Pattern(regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}", message = PHONE_FORMAT)
    private String telefone;

    @NotBlank(message = NOT_BLANK)
    @Email(message = EMAIL_FORMAT)
    private String email;
}
