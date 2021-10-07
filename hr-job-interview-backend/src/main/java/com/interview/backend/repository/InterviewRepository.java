package com.interview.backend.repository;

import com.interview.backend.model.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {

    @Query(value = "select i from Interview i where " +
            "(:name is null or lower(i.candidate.name) like lower(concat_ws('','%', :name,'%')) ) and " +
            "(:surname is null or lower(i.candidate.surname) like lower(concat_ws('','%', :surname,'%')) ) and " +
            "(:email is null or i.candidate.email like lower(concat_ws('','%', :email,'%')) ) and " +
            "(:mobile is null or i.candidate.mobile like concat_ws('','%', :mobile,'%') ) ")
    List<Interview> searchInterviews(@Param("name") String name, @Param("surname") String surname, @Param("email") String email, @Param("mobile") String mobile);
}