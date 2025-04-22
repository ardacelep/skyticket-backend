package com.skylab.skyticket.api.controllers;

import com.skylab.skyticket.business.abstracts.EventService;
import com.skylab.skyticket.core.helpers.BaseResponseHelpers;
import com.skylab.skyticket.core.results.BaseResponse;
import com.skylab.skyticket.core.results.MessageType;
import com.skylab.skyticket.entities.Event;
import com.skylab.skyticket.entities.dtos.ticket.GetEventDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final BaseResponseHelpers baseResponseHelpers;
    private EventService eventService;

    public EventController(EventService eventService, BaseResponseHelpers baseResponseHelpers) {
        this.eventService = eventService;
        this.baseResponseHelpers = baseResponseHelpers;
    }


    @PostMapping("/addEvent")
    public ResponseEntity<?> addEvent(@RequestBody Event event){
        var result = eventService.addEvent(event);

        return ResponseEntity.status(result.getHttpStatus()).body(result);
    }


    @GetMapping("/getEventById/{eventId}")
    public ResponseEntity<?> getEventById(@PathVariable String eventId){
        var result = eventService.getEventById(eventId);

        return ResponseEntity.status(result.getHttpStatus()).body(result);
    }

    @GetMapping("/list")
    public ResponseEntity<BaseResponse<List<GetEventDto>>> getAllEvents(WebRequest webRequest){

        List<GetEventDto> responseData = eventService.getAllEvents();
        BaseResponse<List<GetEventDto>> responseBody = baseResponseHelpers.createBaseResponse(HttpStatus.OK, MessageType.FOUND, "Events were successfully retrieved.", responseData, webRequest);
        return ResponseEntity.status(responseBody.getHttpStatus()).body(responseBody);
    }


}
