package com.skylab.skyticket.business.concretes;

import com.skylab.skyticket.business.abstracts.EventService;
import com.skylab.skyticket.business.abstracts.UserService;
import com.skylab.skyticket.business.constants.Messages;
import com.skylab.skyticket.core.results.*;
import com.skylab.skyticket.core.utilities.ReflectionUtils;
import com.skylab.skyticket.dataAccess.EventDao;
import com.skylab.skyticket.entities.Event;
import com.skylab.skyticket.entities.dtos.ticket.GetEventDto;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class EventManager implements EventService {

    private final EventDao eventDao;

    private final UserService userService;


    public EventManager(EventDao eventDao, UserService userService) {
        this.eventDao = eventDao;
        this.userService = userService;
    }

    @Override
    public Result addEvent(Event event) {
        var authenticatedUserResult = userService.getAuthenticatedUser();
        if (!authenticatedUserResult.isSuccess()){
            return authenticatedUserResult;
        }

        if (event.getName()==null || event.getName().isEmpty()){
           return new ErrorResult(Messages.eventNameCannotBeNull, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        event.setOrganizers(List.of(authenticatedUserResult.getData()));

        eventDao.save(event);

        return new SuccessResult(Messages.eventAdded, HttpStatus.CREATED);

    }

    @Override
    public DataResult<Event> getEventById(String eventId) {
        var event = eventDao.findById(UUID.fromString(eventId));

        if (event.isEmpty()){
            return new ErrorDataResult<>(Messages.eventNotFound, HttpStatus.NOT_FOUND);
        }

        return new SuccessDataResult<>(event.get(), Messages.eventFound, HttpStatus.OK);
    }

    @Override
    public List<GetEventDto> getAllEvents() {
        List<Event> events = eventDao.findAll();

        if (events.isEmpty()) return new ArrayList<>();

        List<GetEventDto> eventDtos = new ArrayList<>();

        for (Event event : events) {
            GetEventDto eventDto = new GetEventDto();
            ReflectionUtils.copyNonNullProperties(event,eventDto);
            eventDtos.add(eventDto);
        }
        return eventDtos;
    }

    public List<GetEventDto> searchEventsByNameOrDescription(String keyword) {
        List<Event> events = new ArrayList<>(eventDao.searchByNameOrDescription(keyword));

        if (events.isEmpty()) return new ArrayList<>();

        List<GetEventDto> eventDtos = new ArrayList<>();

        for (Event event : events) {
            GetEventDto eventDto = new GetEventDto();
            BeanUtils.copyProperties(event,eventDto);
            eventDtos.add(eventDto);
        }
        return eventDtos;
    }


}
