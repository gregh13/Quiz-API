package com.cooksys.quiz_api.controllers.advice;

import com.cooksys.quiz_api.dtos.ErrorDto;
import com.cooksys.quiz_api.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = { "com.cooksys.quiz_api.controllers"} )
@ResponseBody
public class QuizControllerAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ErrorDto handleBadRequestException(BadRequestException badRequestException) {
        return new ErrorDto(badRequestException.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorDto handleNotFoundException(NotFoundException notFoundException) {
        return new ErrorDto(notFoundException.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DeletedAlreadyException.class)
    public ErrorDto handleDeletedAlreadyException(DeletedAlreadyException deletedAlreadyException) {
        return new ErrorDto(deletedAlreadyException.getMessage());
    }
}
