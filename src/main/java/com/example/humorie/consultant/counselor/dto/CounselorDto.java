package com.example.humorie.consultant.counselor.dto;

import lombok.Data;

import java.util.Set;


@Data
public class CounselorDto {

    private long counselorId;

    private String name;

    private Set<String> symptoms;

}
