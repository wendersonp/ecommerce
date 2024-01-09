package com.wendersonp.receiver.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.wendersonp.receiver.domain.util.ValidationMessages.*;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
    @NotNull(message = NOT_NULL)
    @Positive(message = POSITIVE)
    private Integer sku;

    @NotNull(message = NOT_NULL)
    @Positive(message = POSITIVE)
    private Integer quantidade;

    @NotNull(message = NOT_NULL)
    @Positive(message = POSITIVE)
    private Integer valor;
}
