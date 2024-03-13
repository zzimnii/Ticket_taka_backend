package umc.tickettaka.service;

import umc.tickettaka.domain.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {

    void saveRefreshToken(String accessToken, String refreshToken);

    RefreshToken findByAccessToken(String accessToken);

    void deleteRefreshToken(String accessToken);
}
