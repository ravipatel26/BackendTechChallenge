package com.coding.challenge.booking.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include. NON_NULL)
public class ErrorOutput {

    private String errorMessage;
    private List<String> errorMessages;
}
