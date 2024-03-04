package umc.tickettaka.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import umc.tickettaka.domain.RefreshToken;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    Optional<RefreshToken> findByAccessToken(String accessToken);

}
