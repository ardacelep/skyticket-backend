package com.skylab.skyticket.entities.dtos.certificate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.skylab.skyticket.entities.dtos.ticket.GetEventDto;
import com.skylab.skyticket.entities.dtos.ticket.GetUserDto;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetCertificateDto {

    private UUID id;

    private String name;

    private String description;

    private GetEventDto eventDto;

    List<GetUserDto> ownersDto = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String storedLink;

    private Integer nameboxCenterX;

    private Integer nameboxCenterY;

}
