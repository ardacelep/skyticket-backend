package com.skylab.skyticket.core.helpers;

import com.skylab.skyticket.core.exception.ErrorMessageType;
import com.skylab.skyticket.core.exception.RuntimeBaseException;
import com.skylab.skyticket.entities.Certificate;
import com.skylab.skyticket.entities.User;
import com.skylab.skyticket.entities.dtos.certificate.GetCertificateDto;
import com.skylab.skyticket.entities.dtos.ticket.GetEventDto;
import com.skylab.skyticket.entities.dtos.ticket.GetUserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CertificateHelpers {

    public GetCertificateDto convertCertificateToDto(Certificate certificate, boolean includeOwners) {

        GetCertificateDto getCertificateDto = new GetCertificateDto();

        BeanUtils.copyProperties(certificate,getCertificateDto);

        if (certificate.getEvent() == null) {
            throw new RuntimeBaseException(ErrorMessageType.NO_RECORD_EXISTS,"No event found for this certificate.", HttpStatus.NOT_FOUND);
        }

        GetEventDto getEventDto = new GetEventDto();

        BeanUtils.copyProperties(certificate.getEvent(),getEventDto);

        getCertificateDto.setEventDto(getEventDto);

        if (includeOwners){
            List<User> owners = certificate.getOwners();

            for (User owner : owners) {
                GetUserDto tempOwnerDto = new GetUserDto();

                BeanUtils.copyProperties(owner,tempOwnerDto);

                getCertificateDto.getOwnersDto().add(tempOwnerDto);
            }
        }
        else{
            getCertificateDto.setOwnersDto(null);
        }
        return getCertificateDto;
    }
}
