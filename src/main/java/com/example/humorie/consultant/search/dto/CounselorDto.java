package com.example.humorie.consultant.search.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CounselorDto {

    private long counselorId;

    private String name;

    private String gender;

    private String region;

    private double rating;

    private int reviewCount;

    private Set<String> counselingMethods;

    private Set<String> counselingFields;

}
