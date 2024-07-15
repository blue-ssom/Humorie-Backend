package com.example.humorie.consultant.counselor.service;

import com.example.humorie.account.entity.AccountDetail;
import com.example.humorie.account.jwt.JwtTokenUtil;
import com.example.humorie.account.repository.AccountRepository;
import com.example.humorie.consultant.counselor.dto.BookmarkDto;
import com.example.humorie.consultant.counselor.dto.CounselorDto;
import com.example.humorie.consultant.counselor.entity.Bookmark;
import com.example.humorie.consultant.counselor.entity.CounselingField;
import com.example.humorie.consultant.counselor.entity.Counselor;
import com.example.humorie.consultant.counselor.repository.BookmarkRepository;
import com.example.humorie.consultant.counselor.repository.CounselingFieldRepository;
import com.example.humorie.consultant.counselor.repository.CounselorRepository;
import com.example.humorie.global.exception.ErrorCode;
import com.example.humorie.global.exception.ErrorException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final CounselorRepository counselorRepository;
    private final AccountRepository accountRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final CounselingFieldRepository fieldRepository;

    @Transactional
    public String addBookmark(String accessToken, long counselorId) {
        String email = jwtTokenUtil.getEmailFromToken(accessToken);

        AccountDetail account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new ErrorException(ErrorCode.NONE_EXIST_USER));

        Counselor counselor = counselorRepository.findById(counselorId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NON_EXIST_COUNSELOR));

        if (bookmarkRepository.existsByAccountAndCounselor(account, counselor)) {
            throw new ErrorException(ErrorCode.BOOKMARK_EXISTS);
        }

        Bookmark bookmark = Bookmark.builder()
                .account(account)
                .counselor(counselor)
                .build();

        bookmarkRepository.save(bookmark);

        return "Bookmark added successfully";
    }

    @Transactional
    public String removeBookmark(String accessToken, long counselorId) {
        String email = jwtTokenUtil.getEmailFromToken(accessToken);

        AccountDetail account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new ErrorException(ErrorCode.NONE_EXIST_USER));

        Counselor counselor = counselorRepository.findById(counselorId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NON_EXIST_COUNSELOR));

        Bookmark bookmark = bookmarkRepository.findByAccountAndCounselor(account, counselor)
                .orElseThrow(() -> new ErrorException(ErrorCode.NONE_EXIST_BOOKMARK));


        bookmarkRepository.deleteByAccountAndCounselor(account, counselor);

        return "Bookmark removed successfully";
    }

    @Transactional
    public List<BookmarkDto> getAllBookmarks(String accessToken) {
        String email = jwtTokenUtil.getEmailFromToken(accessToken);

        AccountDetail account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new ErrorException(ErrorCode.NONE_EXIST_USER));

        List<Bookmark> bookmarks = bookmarkRepository.findAllByAccount(account);

        return bookmarks.stream()
                .map(this::convertToBookmarkDto)
                .collect(Collectors.toList());
    }

    private BookmarkDto convertToBookmarkDto(Bookmark bookmark) {
        BookmarkDto bookmarkDto = new BookmarkDto();
        bookmarkDto.setBookmarkId(bookmark.getId());
        bookmarkDto.setCreatedAt(bookmark.getCreatedAt());

        CounselorDto counselorDto = convertToCounselorDto(bookmark.getCounselor());
        bookmarkDto.setCounselor(counselorDto);

        return bookmarkDto;
    }

    private CounselorDto convertToCounselorDto(Counselor counselor) {
        CounselorDto counselorDto = new CounselorDto();

        counselorDto.setCounselorId(counselor.getId());
        counselorDto.setName(counselor.getName());

        Set<String> counselingFields = fieldRepository.findByCounselorId(counselor.getId()).stream()
                .map(CounselingField::getField)
                .collect(Collectors.toSet());
        counselorDto.setCounselingFields(counselingFields);

        return counselorDto;
    }

}
