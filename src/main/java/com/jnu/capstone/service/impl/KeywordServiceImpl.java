package com.jnu.capstone.service.impl;

import com.jnu.capstone.dto.KeywordCreateRequestDto;
import com.jnu.capstone.entity.BoardType;
import com.jnu.capstone.entity.Keyword;
import com.jnu.capstone.entity.User;
import com.jnu.capstone.repository.KeywordRepository;
import com.jnu.capstone.repository.UserRepository;
import com.jnu.capstone.service.KeywordService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KeywordServiceImpl implements KeywordService {

    private final KeywordRepository keywordRepository;
    private final UserRepository userRepository;

    public KeywordServiceImpl(KeywordRepository keywordRepository, UserRepository userRepository) {
        this.keywordRepository = keywordRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createKeyword(int userId, KeywordCreateRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

        Keyword keyword = new Keyword();
        keyword.setUser(user);
        keyword.setKeywordText(requestDto.getKeywordText());
        keyword.setBoardType(requestDto.getBoardType());

        keywordRepository.save(keyword);
    }

    @Override
    public List<String> getKeywordsByBoardType(int userId, String boardType) {
        BoardType type = BoardType.valueOf(boardType);
        return keywordRepository.findByUser_UserIdAndBoardType(userId, type)
                .stream()
                .map(Keyword::getKeywordText)
                .collect(Collectors.toList());
    }
}
