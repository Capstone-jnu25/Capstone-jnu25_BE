package com.jnu.capstone.service.impl;

import com.jnu.capstone.dto.SchoolCreateRequestDto;
import com.jnu.capstone.entity.School;
import com.jnu.capstone.repository.SchoolRepository;
import com.jnu.capstone.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SchoolServiceImpl implements SchoolService {

    @Autowired
    private SchoolRepository schoolRepository;

    @Override
    @Transactional
    public School createSchool(SchoolCreateRequestDto requestDto) {
        // 캠퍼스 중복 체크
        if (schoolRepository.existsByCampusName(requestDto.getCampusName())) {
            throw new IllegalArgumentException("이미 존재하는 캠퍼스입니다.");
        }

        // School 엔티티 생성 및 저장
        School school = new School();
        school.setCampusName(requestDto.getCampusName());
        school.setLatitude(requestDto.getLatitude());
        school.setLongitude(requestDto.getLongitude());
        return schoolRepository.save(school);
    }
}
