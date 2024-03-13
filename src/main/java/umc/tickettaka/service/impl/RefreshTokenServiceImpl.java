package umc.tickettaka.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.tickettaka.domain.RefreshToken;
import umc.tickettaka.payload.exception.GeneralException;
import umc.tickettaka.payload.status.ErrorStatus;
import umc.tickettaka.repository.RefreshTokenRepository;
import umc.tickettaka.service.RefreshTokenService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public void saveRefreshToken(String accessToken, String refreshToken) {
        RefreshToken newRefreshToken = RefreshToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        refreshTokenRepository.save(newRefreshToken);
    }

    @Override
    public RefreshToken findByAccessToken(String accessToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new GeneralException(ErrorStatus.EXPIRED_REFRESH_TOKEN, "refresh token이 만료되어 redis에 없습니다."));

        return refreshToken;
    }

    @Override
    @Transactional
    public void deleteRefreshToken(String accessToken) {
        refreshTokenRepository.findByAccessToken(accessToken).ifPresent(refreshTokenRepository::delete);
    }

}
