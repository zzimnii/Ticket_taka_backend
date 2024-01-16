package umc.tickettaka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.tickettaka.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String userName);

    Optional<Member> findByEmail(String email);
}
