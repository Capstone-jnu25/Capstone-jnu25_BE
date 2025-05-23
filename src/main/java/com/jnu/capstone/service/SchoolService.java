package com.jnu.capstone.service;

import com.jnu.capstone.dto.SchoolCreateRequestDto;
import com.jnu.capstone.entity.School;

public interface SchoolService {
    School createSchool(SchoolCreateRequestDto requestDto);
}
