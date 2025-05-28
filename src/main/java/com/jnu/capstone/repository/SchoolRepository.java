package com.jnu.capstone.repository;

import com.jnu.capstone.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<School, Integer> {
    Optional<School> findByCampusName(String campusName);
    boolean existsByCampusName(String campusName);
}
