package com.cooksys.quiz_api.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class DeletedAlreadyException extends RuntimeException {

    private static final long serialVersionUID = 5764741429915694191L;

    private String message;
}
