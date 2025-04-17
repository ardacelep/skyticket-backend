package com.skylab.skyticket.business.abstracts;

import com.skylab.skyticket.entities.dtos.certificate.AddCertificateDto;
import com.skylab.skyticket.entities.dtos.certificate.GetCertificateDto;

import java.util.UUID;

public interface CertificateService {

    GetCertificateDto addCertificate(AddCertificateDto addCertificateDto);

    GetCertificateDto getCertificateById(UUID certificateId);

}
