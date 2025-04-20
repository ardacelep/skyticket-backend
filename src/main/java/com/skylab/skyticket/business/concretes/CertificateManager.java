package com.skylab.skyticket.business.concretes;

import com.skylab.skyticket.business.abstracts.CertificateService;
import com.skylab.skyticket.business.abstracts.UserService;
import com.skylab.skyticket.core.exception.ErrorMessageType;
import com.skylab.skyticket.core.exception.RuntimeBaseException;
import com.skylab.skyticket.core.helpers.CertificateHelpers;
import com.skylab.skyticket.core.results.DataResult;
import com.skylab.skyticket.core.utilities.ReflectionUtils;
import com.skylab.skyticket.dataAccess.CertificateDao;
import com.skylab.skyticket.dataAccess.EventDao;
import com.skylab.skyticket.dataAccess.UserDao;
import com.skylab.skyticket.entities.Certificate;
import com.skylab.skyticket.entities.Event;
import com.skylab.skyticket.entities.User;
import com.skylab.skyticket.entities.dtos.certificate.AddCertificateDto;
import com.skylab.skyticket.entities.dtos.certificate.GetCertificateDto;
import com.skylab.skyticket.entities.dtos.certificate.GiveCertificateDto;
import com.skylab.skyticket.entities.dtos.ticket.GetUserDto;
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
    private final UserService userService;
    private final UserDao userDao;

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

    @Override
    @Transactional
    public GetCertificateDto giveCertificateToUserByEmail(GiveCertificateDto giveCertificateDto) {

        DataResult<User> userResult = userService.getUserByEmail(giveCertificateDto.getUserEmail());

        if (!userResult.isSuccess()) {
            throw new RuntimeBaseException(ErrorMessageType.NO_RECORD_EXISTS, MessageFormat.format("given user does not exist, searched in users for email: {0}", giveCertificateDto.getUserEmail()), HttpStatus.NOT_FOUND);
        }

        Optional<Certificate> optionalCertificate = certificateDao.findById(giveCertificateDto.getCertificateId());

        if (optionalCertificate.isEmpty()){
            throw new RuntimeBaseException(ErrorMessageType.NO_RECORD_EXISTS, MessageFormat.format("given certificate does not exist, searched in certificates for id: {0}", giveCertificateDto.getCertificateId()), HttpStatus.NOT_FOUND);
        }

        User user = (User) userResult.getData();

        Certificate certificate = optionalCertificate.get();

        if (userDao.existsUserHasCertificate(user.getId(), certificate.getId())){
            throw new RuntimeBaseException(ErrorMessageType.RECORD_ALREADY_EXISTS, MessageFormat.format("user with the email {0} already has the certificate with the id of: {1}", giveCertificateDto.getUserEmail(),giveCertificateDto.getCertificateId()), HttpStatus.CONFLICT);
        }

        certificate.getOwners().add(user);
        user.getCertificates().add(certificate);

        certificateDao.save(certificate);
        userDao.save(user);

        GetCertificateDto returnCertificate = certificateHelpers.convertCertificateToDto(certificate, false);

        GetUserDto returnUser = new GetUserDto();

        ReflectionUtils.copyNonNullProperties(user,returnUser);

        returnCertificate.getOwnersDto().add(returnUser);

        return returnCertificate;
    }
}
