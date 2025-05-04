package com.skylab.skyticket.business.concretes;

import com.skylab.skyticket.business.abstracts.CertificateService;
import com.skylab.skyticket.business.abstracts.UserService;
import com.skylab.skyticket.core.exception.ErrorMessageType;
import com.skylab.skyticket.core.exception.RuntimeBaseException;
import com.skylab.skyticket.core.helpers.CertificateHelpers;
import com.skylab.skyticket.core.mail.EmailService;
import com.skylab.skyticket.core.results.DataResult;
import com.skylab.skyticket.core.security.JwtService;
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
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private final JwtService jwtService;
    private final EmailService emailService;

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
    public List<GetCertificateDto> getAllCertificates(boolean includeOwners) {

        List<Certificate> certificates = certificateDao.findAll();

        if (certificates.isEmpty()) return new ArrayList<>();

        List<GetCertificateDto> certificateDtos = new ArrayList<>();

        for (Certificate certificate : certificates) {
            GetCertificateDto certificateDto = certificateHelpers.convertCertificateToDto(certificate,includeOwners);
            certificateDtos.add(certificateDto);
        }

        return certificateDtos;
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

        User user = userResult.getData();

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

    @Override
    public List<GetCertificateDto> getCertificatesByUserEmail(String email) {

        DataResult<User> userResult = userService.getUserByEmail(email);

        if (!userResult.isSuccess()) {
            throw new RuntimeBaseException(ErrorMessageType.NO_RECORD_EXISTS, MessageFormat.format("given user does not exist, searched in users for email: {0}", email), HttpStatus.NOT_FOUND);
        }

        User user = userResult.getData();

        List<Certificate> certificates = certificateDao.findByOwners_Id(user.getId());

        if (certificates.isEmpty()) return new ArrayList<>();

        List<GetCertificateDto> certificateDtos = new ArrayList<>();

        for (Certificate certificate : certificates) {
            GetCertificateDto certificateDto = certificateHelpers.convertCertificateToDto(certificate,false);
            certificateDtos.add(certificateDto);
        }

        return certificateDtos;
    }

    @Override
    public List<GetCertificateDto> getCertificatesByEventId(UUID eventId, boolean includeOwners) {

        Optional<Event> optional = eventDao.findById(eventId);

        if (optional.isEmpty()) throw new RuntimeBaseException(ErrorMessageType.NO_RECORD_EXISTS, MessageFormat.format("given event does not exist, searched in events for id: {0}", eventId), HttpStatus.NOT_FOUND);

        List<Certificate> certificates= certificateDao.findAllByEventId(eventId);

        if (certificates.isEmpty()) return new ArrayList<>();

        List<GetCertificateDto> certificateDtos = new ArrayList<>();

        for (Certificate certificate : certificates) {
            GetCertificateDto certificateDto = certificateHelpers.convertCertificateToDto(certificate,includeOwners);
            certificateDtos.add(certificateDto);
        }
        return certificateDtos;
    }

    @Override
    public GetUserDto sendCheckCertificatesAndTicketsMailToUser(String email) {

        DataResult<User> userResult = userService.getUserByEmail(email);

        if (!userResult.isSuccess()) {
            throw new RuntimeBaseException(ErrorMessageType.NO_RECORD_EXISTS, MessageFormat.format("given user does not exist, searched in users for email: {0}", email), HttpStatus.NOT_FOUND);
        }

        User user = userResult.getData();

        String token = jwtService.generateToken(user.getEmail(), user.getAuthorities());

        String body= "<body style=\"margin:10px;padding:0 20px;font-family:Arial,sans-serif;background-color:#f8f8f8\">\n" +
                "<div style=\"padding:0 20px;border:2px solid #000;box-shadow:8px 8px 0 rgba(0,0,0,.75);background-color:#fff\">\n" +
                "<h1>Sertifika ve Bilet Sorgulama Bağlantısı</h1>\n" +
                "<p>Aşağıdaki butona tıklayarak Sky Lab'in etkinliklerinden edinmiş olduğunuz bilet ve sertifikalara ulaşabilirsiniz.</p>\n" +
                "<a href=\"https://TEMPORARYURL.com/check?token="+token+"\" target=_blank style=\"display:inline-block;background-color:#fd4509;color:#fff;text-decoration:none;font-size:16px;margin-bottom:6px;padding:15px 30px;border:2px solid #000;box-shadow:8px 8px 0 rgba(0,0,0,.75)\">Katılmak için Tıkla</a>\n" +
                "<p style=font-size:12px>Buton çalışmıyor ise bu <a href=\"https://TEMPORARYURL.com/check?token="+token+"\">link</a> üzerinden katılabilirsiniz. <br>Unutmayınız, link kişiye özeldir. <b>Kimse ile paylaşmayınız.<b></b></p>\n" +
                "</div>\n" +
                "</body>";

        var mailResult = emailService.sendMail(user.getEmail(), "Sky Lab Sertifika ve Bilet Sorgulama Bağlantısı", body);

        if (!mailResult.isSuccess()) {
            throw new RuntimeBaseException(ErrorMessageType.MAIL_ERROR, MessageFormat.format("an error occurred while sending mail to: {0}", user.getEmail()), HttpStatus.SERVICE_UNAVAILABLE);
        }

        GetUserDto returnUser = new GetUserDto();
        BeanUtils.copyProperties(user,returnUser);

        return returnUser;
    }
}
