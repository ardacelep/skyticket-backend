package com.skylab.skyticket.business.abstracts;

import com.skylab.skyticket.entities.dtos.certificate.AddCertificateDto;
import com.skylab.skyticket.entities.dtos.certificate.GetCertificateDto;
import com.skylab.skyticket.entities.dtos.certificate.GiveCertificateDto;

import java.util.List;
import java.util.UUID;

public interface CertificateService {

    GetCertificateDto addCertificate(AddCertificateDto addCertificateDto);

    GetCertificateDto getCertificateById(UUID certificateId, boolean includeOwners);

    GetCertificateDto giveCertificateToUserByEmail(GiveCertificateDto giveCertificateDto);

    List<GetCertificateDto> getAllCertificates(boolean includeOwners);

}
