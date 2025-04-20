package com.skylab.skyticket.core.results;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum MessageType {

    CREATED("CREATED", "Data was successfully created and saved to the database."),
    FOUND("FOUND", "Data was successfully retrieved from the database."),
    COMPLETED("COMPLETED", "Process was successfully completed."),;


    @JsonProperty("name")
    private final String name;

    @JsonProperty("message")
    private final String message;

    MessageType(String name, String message) {
        this.name = name;
        this.message = message;
    }

}
