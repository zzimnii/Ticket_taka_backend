package umc.tickettaka.service;

import umc.tickettaka.domain.BlackList;

public interface BlackListService {

    void saveBlackList(String accessToken);

    BlackList findByAccessToken(String accessToken);

    void deleteBlackList(String accessToken);
}
