package com.jnu.capstone.repository;

import com.jnu.capstone.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolRepository extends JpaRepository<School, Integer> {
    boolean existsByCampusName(String campusName);
}
