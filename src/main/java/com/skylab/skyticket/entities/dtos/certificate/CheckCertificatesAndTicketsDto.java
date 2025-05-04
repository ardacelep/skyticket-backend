package com.skylab.skyticket.entities.dtos.certificate;


import com.skylab.skyticket.entities.dtos.ticket.GetTicketDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckCertificatesAndTicketsDto {

    List<GetCertificateDto> getCertificateDtoList;
    List<GetTicketDto> getTicketDtoList;

}
