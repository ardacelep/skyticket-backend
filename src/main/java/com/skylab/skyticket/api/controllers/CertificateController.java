package com.skylab.skyticket.api.controllers;


import com.skylab.skyticket.business.abstracts.CertificateService;
import com.skylab.skyticket.core.helpers.BaseResponseHelpers;
import com.skylab.skyticket.core.results.BaseResponse;
import com.skylab.skyticket.core.results.MessageType;
import com.skylab.skyticket.entities.dtos.certificate.*;
import com.skylab.skyticket.entities.dtos.ticket.GetUserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;
    private final BaseResponseHelpers baseResponseHelpers;

    @PostMapping("/add")
    public ResponseEntity<BaseResponse<GetCertificateDto>>  addCertificate(@RequestBody @Valid AddCertificateDto addCertificateDto, WebRequest webRequest){
        GetCertificateDto responseData = certificateService.addCertificate(addCertificateDto);
        BaseResponse<GetCertificateDto> responseBody = baseResponseHelpers.createBaseResponse(HttpStatus.CREATED, MessageType.CREATED, "Certificate was successfully saved.", webRequest, responseData);
        return ResponseEntity.status(responseBody.getHttpStatus()).body(responseBody);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<BaseResponse<GetCertificateDto>> getCertificateById(@PathVariable UUID id, @RequestParam(defaultValue = "false") boolean includeOwners, WebRequest webRequest){
        GetCertificateDto responseData = certificateService.getCertificateById(id,includeOwners);
        BaseResponse<GetCertificateDto> responseBody = baseResponseHelpers.createBaseResponse(HttpStatus.OK, MessageType.FOUND, MessageFormat.format("Certificate with the id: {0} was successfully retrieved.",id), webRequest, responseData);
        return ResponseEntity.status(responseBody.getHttpStatus()).body(responseBody);
    }

    @PostMapping("/give")
    public ResponseEntity<BaseResponse<GetCertificateDto>> giveCertificateToUserByEmail(@RequestBody @Valid GiveCertificateDto giveCertificateDto, WebRequest webRequest){
        GetCertificateDto responseData = certificateService.giveCertificateToUserByEmail(giveCertificateDto);
        BaseResponse<GetCertificateDto> responseBody = baseResponseHelpers.createBaseResponse(HttpStatus.OK, MessageType.COMPLETED, MessageFormat.format("Certificate was successfully given to user with the email: {0}.",giveCertificateDto.getUserEmail()), webRequest, responseData);
        return ResponseEntity.status(responseBody.getHttpStatus()).body(responseBody);
    }

    @GetMapping("/list")
    public ResponseEntity<BaseResponse<List<GetCertificateDto>>> getAllCertificates(@RequestParam(defaultValue = "false") boolean includeOwners, WebRequest webRequest){
        List<GetCertificateDto> responseData = certificateService.getAllCertificates(includeOwners);
        BaseResponse<List<GetCertificateDto>> responseBody = baseResponseHelpers.createBaseResponse(HttpStatus.OK, MessageType.FOUND, "Certificates were successfully retrieved.", webRequest, responseData);
        return ResponseEntity.status(responseBody.getHttpStatus()).body(responseBody);
    }

    @GetMapping("/user")
    public ResponseEntity<BaseResponse<List<GetCertificateDto>>> getCertificatesByUserEmail(@RequestParam String email, WebRequest webRequest){
        List<GetCertificateDto> responseData = certificateService.getCertificatesByUserEmail(email);
        BaseResponse<List<GetCertificateDto>> responseBody = baseResponseHelpers.createBaseResponse(HttpStatus.OK, MessageType.FOUND, MessageFormat.format("Certificates were successfully retrieved for user with the email: {0}",email), webRequest, responseData);
        return ResponseEntity.status(responseBody.getHttpStatus()).body(responseBody);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<BaseResponse<List<GetCertificateDto>>> getCertificatesByEventId(@PathVariable UUID eventId, @RequestParam(defaultValue = "false") boolean includeOwners, WebRequest webRequest){
        List<GetCertificateDto> responseData = certificateService.getCertificatesByEventId(eventId,includeOwners);
        BaseResponse<List<GetCertificateDto>> responseBody = baseResponseHelpers.createBaseResponse(HttpStatus.OK, MessageType.FOUND, MessageFormat.format("Certificates were successfully retrieved for event with the id: {0}",eventId), webRequest, responseData);
        return ResponseEntity.status(responseBody.getHttpStatus()).body(responseBody);
    }

    @GetMapping("/send")
    public ResponseEntity<BaseResponse<GetUserDto>> sendCheckCertificatesAndTicketsMailToUser(@RequestParam String email, WebRequest webRequest){
        GetUserDto responseData = certificateService.sendCheckCertificatesAndTicketsMailToUser(email);
        BaseResponse<GetUserDto> responseBody = baseResponseHelpers.createBaseResponse(HttpStatus.OK, MessageType.COMPLETED, MessageFormat.format("Mail was successfully sent to user with the email: {0}",email), webRequest, responseData);
        return ResponseEntity.status(responseBody.getHttpStatus()).body(responseBody);
    }

    @GetMapping("/check")
    public ResponseEntity<BaseResponse<CheckCertificatesAndTicketsDto>> checkCertificatesAndTickets(@RequestParam String token, WebRequest webRequest){
        CheckCertificatesAndTicketsDto responseData = certificateService.checkCertificatesAndTicketsByToken(token);
        BaseResponse<CheckCertificatesAndTicketsDto> responseBody = baseResponseHelpers.createBaseResponse(HttpStatus.OK, MessageType.COMPLETED, "Certificates and tickets were successfully retrieved.", webRequest, responseData);
        return ResponseEntity.status(responseBody.getHttpStatus()).body(responseBody);
    }

    @GetMapping("/search")
    public ResponseEntity<BaseResponse<List<GetCertificateDto>>> searchCertificatesByNameOrDescription(@RequestParam String keyword, @RequestParam(defaultValue = "false") boolean includeOwners, WebRequest webRequest){
        List<GetCertificateDto> responseData = certificateService.searchCertificatesByNameOrDescription(keyword,includeOwners);
        BaseResponse<List<GetCertificateDto>> responseBody = baseResponseHelpers.createBaseResponse(HttpStatus.OK, MessageType.FOUND, "Certificates were successfully retrieved.", webRequest, responseData);
        return ResponseEntity.status(responseBody.getHttpStatus()).body(responseBody);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<BaseResponse<GetCertificateDto>> updateCertificate(@PathVariable UUID id, @RequestBody @Valid UpdateCertificateDto updateCertificateDto, WebRequest webRequest){
        GetCertificateDto responseData = certificateService.updateCertificate(id,updateCertificateDto);
        BaseResponse<GetCertificateDto> responseBody = baseResponseHelpers.createBaseResponse(HttpStatus.OK, MessageType.UPDATED, MessageFormat.format("Certificate with the id: {0} was successfully updated.",id), webRequest, responseData);
        return ResponseEntity.status(responseBody.getHttpStatus()).body(responseBody);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BaseResponse<GetCertificateDto>> deleteCertificate(@PathVariable UUID id, WebRequest webRequest){
        GetCertificateDto responseData = certificateService.deleteCertificate(id);
        BaseResponse<GetCertificateDto> responseBody = baseResponseHelpers.createBaseResponse(HttpStatus.OK, MessageType.DELETED, MessageFormat.format("Certificate with the id: {0} was successfully deleted.",id), webRequest, responseData);
        return ResponseEntity.status(responseBody.getHttpStatus()).body(responseBody);
    }

}
