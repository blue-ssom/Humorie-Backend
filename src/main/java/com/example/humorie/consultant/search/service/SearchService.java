package com.example.humorie.consultant.search.service;

import com.example.humorie.consultant.counselor.entity.Counselor;
import com.example.humorie.consultant.counselor.entity.Symptom;
import com.example.humorie.consultant.counselor.repository.CounselorRepository;
import com.example.humorie.consultant.search.dto.CounselorDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchService {

    private final CounselorRepository counselorRepository;

    public List<CounselorDto> searchCounselor(String keyword) {
        List<Counselor> counselorList = counselorRepository.findAll();
        List<CounselorDto> result = new ArrayList<>();

        try {
            for (Counselor counselor : counselorList) {
                if (counselor.getName().contains(keyword)) {
                    result.add(new CounselorDto(counselor.getId(), counselor.getName(), counselor.getCounselingFields(), counselor.getSpecialties()));
                } else {
                    List<Symptom> matchingSymptoms = counselor.getSpecialties().stream()
                            .filter(symptom -> symptom.getKoreanName().contains(keyword))
                            .collect(Collectors.toList());
                    if (!matchingSymptoms.isEmpty()) {
                        result.add(new CounselorDto(counselor.getId(), counselor.getName(), counselor.getCounselingFields(), counselor.getSpecialties()));
                    }
                }
            }
        } catch (Exception e) {
            log.error("An error occurred while searching for counselors", e);
        }

        return result;
    }

}