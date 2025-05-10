package kz.ssss.filo.repository;

import kz.ssss.filo.model.UserQuota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserQuotaRepository extends JpaRepository<UserQuota, Long> {
}
