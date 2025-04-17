package com.skylab.skyticket.business.concretes;

import com.skylab.skyticket.business.abstracts.CertificateService;
import com.skylab.skyticket.core.helpers.CertificateHelper;
import com.skylab.skyticket.core.utilities.ReflectionUtils;
import com.skylab.skyticket.dataAccess.CertificateDao;
import com.skylab.skyticket.entities.Certificate;
import com.skylab.skyticket.entities.dtos.certificate.AddCertificateDto;
import com.skylab.skyticket.entities.dtos.certificate.GetCertificateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CertificateManager implements CertificateService {

    private final CertificateDao certificateDao;
    private final CertificateHelper certificateHelper;

    @Override
    @Transactional
    public GetCertificateDto addCertificate(AddCertificateDto addCertificateDto) {

        Certificate certificate = new Certificate();

        ReflectionUtils.copyNonNullProperties(addCertificateDto,certificate);

        certificateDao.save(certificate);

        return certificateHelper.convertCertificateToDto(certificate,false);
    }

    @Override
    public GetCertificateDto getCertificateById(UUID certificateId) {
        return null;
    }
}
