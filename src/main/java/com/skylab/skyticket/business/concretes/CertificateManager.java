package com.skylab.skyticket.business.concretes;

import com.skylab.skyticket.business.abstracts.CertificateService;
import com.skylab.skyticket.core.exception.ErrorMessageType;
import com.skylab.skyticket.core.exception.RuntimeBaseException;
import com.skylab.skyticket.core.helpers.CertificateHelpers;
import com.skylab.skyticket.core.utilities.ReflectionUtils;
import com.skylab.skyticket.dataAccess.CertificateDao;
import com.skylab.skyticket.dataAccess.EventDao;
import com.skylab.skyticket.entities.Certificate;
import com.skylab.skyticket.entities.Event;
import com.skylab.skyticket.entities.dtos.certificate.AddCertificateDto;
import com.skylab.skyticket.entities.dtos.certificate.GetCertificateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CertificateManager implements CertificateService {

    private final CertificateDao certificateDao;
    private final CertificateHelpers certificateHelpers;
    private final EventDao eventDao;

    @Override
    @Transactional
    public GetCertificateDto addCertificate(AddCertificateDto addCertificateDto) {

        UUID eventId = addCertificateDto.getEventId();

        Optional<Event> optional = eventDao.findById(eventId);

        if (optional.isEmpty()) throw new RuntimeBaseException(ErrorMessageType.NO_RECORD_EXISTS, MessageFormat.format("given event does not exist, searched in events for id: {0}", eventId), HttpStatus.NOT_FOUND);

        Certificate certificate = new Certificate();

        ReflectionUtils.copyNonNullProperties(addCertificateDto,certificate);

        if (certificate.getCreatedAt() == null) certificate.setCreatedAt(LocalDateTime.now());

        certificate.setEvent(optional.get());

        certificateDao.save(certificate);

        return certificateHelpers.convertCertificateToDto(certificate,false);
    }

    @Override
    public GetCertificateDto getCertificateById(UUID certificateId, boolean includeOwners) {

        Optional<Certificate> optional = certificateDao.findById(certificateId);

        if (optional.isEmpty()) throw new RuntimeBaseException(ErrorMessageType.NO_RECORD_EXISTS, MessageFormat.format("given certificate does not exist, searched in certificates for id: {0}", certificateId), HttpStatus.NOT_FOUND);

        return certificateHelpers.convertCertificateToDto(optional.get(),includeOwners);
    }
}
