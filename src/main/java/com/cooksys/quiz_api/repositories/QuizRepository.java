package com.cooksys.quiz_api.repositories;

import com.cooksys.quiz_api.entities.Quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
// import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    List<Quiz> findAllByIsDeletedFalse();

    Quiz findByIdAndIsDeletedFalse(Long id);

//    // NOTE: Saving to keep as a reference
//    // Inefficient, but working way to filter out deleted quizzes and deleted questions in a single query
//    @Query("SELECT DISTINCT q FROM Quiz q JOIN FETCH q.questions qs WHERE q.isDeleted = false AND qs.isDeleted = false")
//    List<Quiz> findAllWithNonDeletedQuestions();
}
