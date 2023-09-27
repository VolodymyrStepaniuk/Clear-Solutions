package com.stepaniuk.clear_solutions.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>{
    List<User> findAllByBirthDateBetween(LocalDate fromDate, LocalDate toDate);
}
