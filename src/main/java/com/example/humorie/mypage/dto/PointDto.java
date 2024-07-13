package com.example.humorie.mypage.dto;

import com.example.humorie.mypage.entity.Point;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointDto {

    private long pointId;

    private int earnedPoints;

    private int spentPoints;

    private LocalDateTime transactionDate;

    public static PointDto convertToPointDto(Point point) {
        int earnedPoint = point.getType().equals("earn") ? point.getPoints() : 0;
        int spentPoint = point.getType().equals("spend") ? point.getPoints() : 0;

        return PointDto.builder()
                .pointId(point.getId())
                .earnedPoints(earnedPoint)
                .spentPoints(spentPoint)
                .transactionDate(point.getTransactionDate())
                .build();
    }

}
