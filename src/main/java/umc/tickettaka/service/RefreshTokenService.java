package umc.tickettaka.service;

import umc.tickettaka.domain.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {

    public void saveRefreshToken(String accessToken, String refreshToken);

    RefreshToken findByAccessToken(String accessToken);
}
