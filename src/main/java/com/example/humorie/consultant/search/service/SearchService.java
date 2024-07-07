package com.example.humorie.consultant.search.service;

import com.example.humorie.consultant.counselor.entity.Counselor;
import com.example.humorie.consultant.counselor.entity.Symptom;
import com.example.humorie.consultant.counselor.repository.CounselorRepository;
import com.example.humorie.consultant.search.dto.CounselorDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchService {

    private final CounselorRepository counselorRepository;

    public List<CounselorDto> getAllCounselors() {
        List<Counselor> counselors = counselorRepository.findAll();

        return counselors.stream()
                .map(CounselorDto::createDto)
                .collect(Collectors.toList());
    }


    public List<CounselorDto> searchByKeywords(List<String> symptomKeywords, String counselingMethod, String gender, String region, String order) {
        List<Counselor> counselorList = counselorRepository.findAll();
        List<CounselorDto> result = new ArrayList<>();

        try {
            for (Counselor counselor : counselorList) {
                List<Symptom> counselorSymptoms = counselor.getSymptoms();

                if (containsAllSymptomKeywords(counselorSymptoms, symptomKeywords)) {
                    result.add(CounselorDto.createDto(counselor));
                }
            }
        } catch (Exception e) {
            log.error("An error occurred while searching for counselors", e);
        }

        return filterByConditions(result, counselingMethod, gender, region, order);
    }

    public List<CounselorDto> searchBySingleKeyword(String keyword, String counselingMethod, String gender, String region, String order) {
        List<Counselor> counselorList = counselorRepository.findAll();
        List<CounselorDto> result = new ArrayList<>();

        try {
            for (Counselor counselor : counselorList) {
                List<Symptom> counselorSymptoms = counselor.getSymptoms();

                if (containsKeywordInSymptoms(counselorSymptoms, keyword)) {
                    result.add(CounselorDto.createDto(counselor));
                }
            }
        } catch (Exception e) {
            log.error("An error occurred while searching for counselors", e);
        }

        return filterByConditions(result, counselingMethod, gender, region, order);
    }

    public List<CounselorDto> searchByConditions(String counselingMethod, String gender, String region, String order) {
        Specification<Counselor> specification = Specification
                .where(CounselorSpecification.hasCounselingMethod(counselingMethod))
                .and(CounselorSpecification.hasGender(gender))
                .and(CounselorSpecification.hasRegion(region))
                .and(CounselorSpecification.orderBy(order));

        List<Counselor> counselors = counselorRepository.findAll(specification);

        return counselors.stream()
                .map(CounselorDto::createDto)
                .collect(Collectors.toList());
    }

    private boolean containsAllSymptomKeywords(List<Symptom> symptoms, List<String> symptomKeywords) {
        List<String> symptomNames = symptoms.stream()
                .map(Symptom::getSymptom)
                .collect(Collectors.toList());

        return symptomNames.containsAll(symptomKeywords);
    }

    private boolean containsKeywordInSymptoms(List<Symptom> symptoms, String keyword) {
        for (Symptom symptom : symptoms) {
            if (symptom.getSymptom().contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    public List<CounselorDto> filterByConditions(List<CounselorDto> counselors, String counselingMethod, String gender, String region, String order) {
        Specification<Counselor> specification = Specification
                .where(CounselorSpecification.hasCounselingMethod(counselingMethod))
                .and(CounselorSpecification.hasGender(gender))
                .and(CounselorSpecification.hasRegion(region))
                .and(CounselorSpecification.orderBy(order));

        List<Counselor> filteredCounselors = counselorRepository.findAll(specification);

        List<CounselorDto> filteredDtoList = filteredCounselors.stream()
                .map(CounselorDto::createDto)
                .collect(Collectors.toList());

        return counselors.stream()
                .filter(counselorDto -> filteredDtoList.contains(counselorDto))
                .collect(Collectors.toList());
    }

}