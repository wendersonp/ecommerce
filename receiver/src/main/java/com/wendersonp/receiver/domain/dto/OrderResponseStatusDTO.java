package com.wendersonp.receiver.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wendersonp.receiver.domain.enumeration.RequestStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseStatusDTO {

    private RequestStatusEnum status;

    @JsonProperty("dataResposta")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime responseDate;
}
