package com.example.humorie.consultant.counselor.dto;

import com.example.humorie.consultant.counselor.entity.CounselingField;
import lombok.Data;

import java.util.Set;

@Data
public class CounselorDto {

    private String name;

    private Set<CounselingField> counselingField;

}
