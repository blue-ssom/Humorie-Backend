package com.example.humorie.consultant.search.dto;

import com.example.humorie.consultant.counselor.entity.CounselingField;
import com.example.humorie.consultant.counselor.entity.Symptom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CounselorDto {

    private long counselorId;

    private String name;

    private Set<CounselingField> counselingFields;

    private List<Symptom> specialties;

}
