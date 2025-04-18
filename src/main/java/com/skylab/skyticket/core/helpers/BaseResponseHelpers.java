package com.skylab.skyticket.core.helpers;

import com.skylab.skyticket.core.results.BaseResponse;
import com.skylab.skyticket.core.results.MessageType;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class BaseResponseHelpers {

    public <D> BaseResponse<D> createBaseResponse(HttpStatus httpStatus, MessageType messageType, String message,D data, WebRequest webRequest) {
        BaseResponse<D> response = new BaseResponse<>();

        response.setHttpStatus(httpStatus);

        response.setHttpStatusCode(httpStatus.value());

        response.setMessageType(messageType);

        response.setData(data);

        response.setCreatedAt(LocalDateTime.now());

        response.setHostName(getHostName());

        response.setPath(webRequest.getDescription(false).substring(4));

        if (webRequest instanceof ServletWebRequest servletWebRequest) {
            HttpServletRequest request = servletWebRequest.getRequest();
            String methodType = request.getMethod(); // GET, POST, PATCH vb.
            response.setRequestType(methodType);
        }

        response.setMessage(message);

        return response;

    }

    public BaseResponse<Void> createBaseResponse(HttpStatus httpStatus, MessageType messageType, String message, WebRequest webRequest) {
        return createBaseResponse(httpStatus, messageType, message, null, webRequest);
    }

    public String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();


        } catch (UnknownHostException e) {
            System.out.println("hata oluştu " + e.getMessage());
        }

        return null;

    }

    public List<String> addMapValue(List<String> list, String newValue) {
        list.add(newValue);
        return list;
    }


}
