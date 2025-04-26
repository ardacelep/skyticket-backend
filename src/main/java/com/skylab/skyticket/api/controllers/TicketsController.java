package com.skylab.skyticket.api.controllers;

import com.skylab.skyticket.business.abstracts.TicketService;
import com.skylab.skyticket.core.helpers.BaseResponseHelpers;
import com.skylab.skyticket.core.results.BaseResponse;
import com.skylab.skyticket.core.results.DataResult;
import com.skylab.skyticket.core.results.MessageType;
import com.skylab.skyticket.entities.dtos.ticket.AddTicketDto;
import com.skylab.skyticket.entities.dtos.ticket.GetTicketDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tickets")
public class TicketsController {


    private final TicketService ticketService;
    private final BaseResponseHelpers baseResponseHelpers;

    public TicketsController(TicketService ticketService, BaseResponseHelpers baseResponseHelpers) {
        this.ticketService = ticketService;
        this.baseResponseHelpers = baseResponseHelpers;
    }

    @PostMapping("/addTicket")
    public ResponseEntity<?> addTicket(@RequestBody AddTicketDto addTicketDto){
        var result = ticketService.addTicket(addTicketDto);

        return ResponseEntity.status(result.getHttpStatus()).body(result);

    }

    @GetMapping("/getTicketById/{ticketId}")
    public ResponseEntity<DataResult<GetTicketDto>> getTicketById(@PathVariable String ticketId){
        var result = ticketService.getTicketById(UUID.fromString(ticketId));

        return ResponseEntity.status(result.getHttpStatus()).body(result);
    }

    @PutMapping("/submitTicket/{ticketId}")
    public ResponseEntity<?> submitTicket(@PathVariable String ticketId){
        var result = ticketService.submitTicket(UUID.fromString(ticketId));

        return ResponseEntity.status(result.getHttpStatus()).body(result);
    }

    @GetMapping("/getTicketsByUserEmail/{email}")
    public ResponseEntity<BaseResponse<List<GetTicketDto>>> getTicketsByUserEmail(@PathVariable String email, WebRequest webRequest){
        List<GetTicketDto> responseData = ticketService.getAllTicketsByUserEmail(email);
        BaseResponse<List<GetTicketDto>> responseBody = baseResponseHelpers.createBaseResponse(HttpStatus.OK, MessageType.FOUND, MessageFormat.format("Tickets were successfully retrieved for user with the email: {0}",email), webRequest, responseData);
        return ResponseEntity.status(responseBody.getHttpStatus()).body(responseBody);
    }


}
