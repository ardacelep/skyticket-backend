package com.skylab.skyticket.business.abstracts;

import com.skylab.skyticket.core.results.DataResult;
import com.skylab.skyticket.core.results.Result;
import com.skylab.skyticket.entities.Event;
import com.skylab.skyticket.entities.dtos.ticket.GetEventDto;

import java.util.List;
import java.util.UUID;

public interface EventService {


    Result addEvent(Event event);

    DataResult<Event> getEventById(String eventId);

    List<GetEventDto> getAllEvents();



}
