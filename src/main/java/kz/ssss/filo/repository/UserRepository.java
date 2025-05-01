package kz.ssss.filo.repository;

import kz.ssss.filo.model.Tenant;
import kz.ssss.filo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u JOIN FETCH u.tenant WHERE u.username = :username")
    Optional<User> findByUsernameWithTenant(@Param("username") String username);

    Optional<User> findByUsernameAndTenant(String username, Tenant tenant);
}
