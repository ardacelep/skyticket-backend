package com.skylab.skyticket.core.results;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<D> {

    private HttpStatus httpStatus;

    private Integer httpStatusCode;

    private MessageType messageType;

    private String hostName;

    private String path;

    private String requestType;

    private LocalDateTime createdAt;

    private String message;

    private D data;

}
