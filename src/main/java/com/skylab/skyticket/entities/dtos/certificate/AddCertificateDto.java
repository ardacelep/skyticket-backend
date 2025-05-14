package com.skylab.skyticket.entities.dtos.certificate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddCertificateDto {

    @NotBlank(message = "name can not be empty.")
    private String name;

    private String description;

    @NotNull(message = "certificate must be for an event. eventId can not be empty.")
    private UUID eventId;

    @PastOrPresent(message = "createdAt must be in the past or present.")
    @NotNull
    private LocalDateTime createdAt;

    @NotBlank(message = "storedLink can not be empty.")
    private String storedLink;

    @NotNull(message = "nameboxCenterX can not be empty")
    @DecimalMin(value = "0.00", inclusive = true, message = "nameboxCenterX must be equal to or higher than 0.00 .")
    private Integer nameboxCenterX;

    @NotNull(message = "nameboxCenterY can not be empty")
    @DecimalMin(value = "0.00", inclusive = true, message = "nameboxCenterY must be equal to or higher than 0.00 .")
    private Integer nameboxCenterY;


}
