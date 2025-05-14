package com.skylab.skyticket.entities.dtos.certificate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GiveCertificateDto {

    @NotNull(message = "certificateId can not be empty.")
    private UUID certificateId;

    @NotBlank(message = "userEmail can not be empty.")
    private String userEmail;

}
