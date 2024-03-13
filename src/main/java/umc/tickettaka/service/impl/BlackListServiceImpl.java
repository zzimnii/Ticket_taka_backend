package umc.tickettaka.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.tickettaka.domain.BlackList;
import umc.tickettaka.repository.BlackListRepository;
import umc.tickettaka.service.BlackListService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlackListServiceImpl implements BlackListService {

    private final BlackListRepository blackListRepository;

    @Override
    public void saveBlackList(String accessToken) {

        BlackList blackList = BlackList.builder()
                .accessToken(accessToken)
                .isLogOut(Boolean.TRUE)
                .build();

        blackListRepository.save(blackList);
    }

    @Override
    public BlackList findByAccessToken(String accessToken) {
        return blackListRepository.findByAccessToken(accessToken)
                .orElse(null);
    }

    @Override
    public void deleteBlackList(String accessToken) {
        blackListRepository.findByAccessToken(accessToken).ifPresent(blackListRepository::delete);
    }
}
