package umc.tickettaka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.tickettaka.domain.ticket.File;

public interface FileRepository extends JpaRepository<File, Long> {
}
