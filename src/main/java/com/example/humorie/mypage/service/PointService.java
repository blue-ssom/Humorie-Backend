package com.example.humorie.mypage.service;

import com.example.humorie.account.entity.AccountDetail;
import com.example.humorie.account.jwt.JwtTokenUtil;
import com.example.humorie.account.repository.AccountRepository;
import com.example.humorie.mypage.dto.PointDto;
import com.example.humorie.mypage.dto.TotalPointDto;
import com.example.humorie.mypage.entity.Point;
import com.example.humorie.mypage.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;
    private final AccountRepository accountRepository;
    private final JwtTokenUtil jwtTokenUtil;

    public List<PointDto> getAllPoints(String accessToken) {
        String email = jwtTokenUtil.getEmailFromToken(accessToken);

        AccountDetail account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Not found user"));

        return pointRepository.findByAccount(account).stream()
                .sorted((t1, t2) -> t1.getTransactionDate().compareTo(t2.getTransactionDate()))
                .map(PointDto::convertToPointDto)
                .collect(Collectors.toList());
    }

    public List<PointDto> getEarnedPoints(String accessToken) {
        String email = jwtTokenUtil.getEmailFromToken(accessToken);

        AccountDetail account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Not found user"));

        return pointRepository.findByAccountAndType(account, "earn").stream()
                .sorted((t1, t2) -> t1.getTransactionDate().compareTo(t2.getTransactionDate()))
                .map(PointDto::convertToPointDto)
                .collect(Collectors.toList());
    }

    public List<PointDto> getSpentPoints(String accessToken) {
        String email = jwtTokenUtil.getEmailFromToken(accessToken);

        AccountDetail account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Not found user"));

        return pointRepository.findByAccountAndType(account, "spend").stream()
                .sorted((t1, t2) -> t1.getTransactionDate().compareTo(t2.getTransactionDate()))
                .map(PointDto::convertToPointDto)
                .collect(Collectors.toList());
    }

    public TotalPointDto getTotalPoints(String accessToken) {
        String email = jwtTokenUtil.getEmailFromToken(accessToken);

        AccountDetail account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Not found user"));

        List<Point> points = pointRepository.findByAccount(account);

        int totalEarnedPoints = points.stream()
                .filter(t -> t.getType().equals("earn"))
                .mapToInt(Point::getPoints)
                .sum();

        int totalSpentPoints = points.stream()
                .filter(t -> t.getType().equals("spend"))
                .mapToInt(Point::getPoints)
                .sum();

        int totalPoints = totalEarnedPoints - totalSpentPoints;

        return TotalPointDto.builder().totalPoints(totalPoints).build();
    }

}
