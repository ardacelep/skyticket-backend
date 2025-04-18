package com.skylab.skyticket.api.controllers;


import com.skylab.skyticket.business.abstracts.CertificateService;
import com.skylab.skyticket.core.helpers.BaseResponseHelpers;
import com.skylab.skyticket.core.results.BaseResponse;
import com.skylab.skyticket.core.results.MessageType;
import com.skylab.skyticket.entities.dtos.certificate.AddCertificateDto;
import com.skylab.skyticket.entities.dtos.certificate.GetCertificateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.UUID;

@RestController
@RequestMapping("/api/certificate")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;
    private final BaseResponseHelpers baseResponseHelpers;

    @PostMapping("/add")
    public BaseResponse<GetCertificateDto> addCertificate(@RequestBody @Valid AddCertificateDto addCertificateDto, WebRequest webRequest){
        GetCertificateDto responseData = certificateService.addCertificate(addCertificateDto);
        return baseResponseHelpers.createBaseResponse(HttpStatus.CREATED, MessageType.CREATED, "Certificate was successfully saved.", responseData, webRequest);
    }

    @PostMapping("/find/{id}")
    public BaseResponse<GetCertificateDto> getCertificateById(@PathVariable UUID id, @RequestParam(defaultValue = "false") boolean includeOwners, WebRequest webRequest){
        GetCertificateDto responseData = certificateService.getCertificateById(id,includeOwners);
        return baseResponseHelpers.createBaseResponse(HttpStatus.OK, MessageType.FOUND, "Certificate was successfully retrieved.", responseData, webRequest);
    }

}
