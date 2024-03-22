package com.cooksys.quiz_api.mappers;

import java.util.List;

import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import com.cooksys.quiz_api.entities.Quiz;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { QuestionMapper.class })
public interface QuizMapper {

  QuizResponseDto entityToResponseDto(Quiz entity);

  List<QuizResponseDto> entitiesToResponseDtos(List<Quiz> entities);

  Quiz requestDtoToEntity(QuizRequestDto quizRequestDto);
}
