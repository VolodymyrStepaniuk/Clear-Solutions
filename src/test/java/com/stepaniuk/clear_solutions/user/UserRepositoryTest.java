package com.stepaniuk.clear_solutions.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @DisplayName("Should find all users by birth date between")
    @Test
    void findAllByBirthDateBetween() {
        User testUser = new User();
        testUser.setEmail("volodymyr@gmail.com");
        testUser.setFirstName("Volodymyr");
        testUser.setLastName("Stepaniuk");
        testUser.setBirthDate(LocalDate.of(2004,5,20));
        testUser.setAddress("Ukraine, Lviv");
        testUser.setPhoneNumber("+380683006791");

        userRepository.save(testUser);

        LocalDate startDate = LocalDate.of(2004,5,10);
        LocalDate endDate = LocalDate.of(2004,5,30);

        List<User> users = Collections.singletonList(testUser);

        assertEquals(1, userRepository.findAllByBirthDateBetween(startDate, endDate).size());
        assertEquals(testUser, users.get(0));
    }
}