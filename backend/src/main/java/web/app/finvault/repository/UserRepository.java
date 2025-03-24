package web.app.finvault.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.app.finvault.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

    User findByUsernameIgnoreCase(String username);

    User findByEmailIgnoreCase(String email);
}
