package umc.tickettaka.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import umc.tickettaka.domain.BlackList;

import java.util.Optional;

@Repository
public interface BlackListRepository extends CrudRepository<BlackList, String> {

    Optional<BlackList> findByAccessToken(String accessToken);

}
