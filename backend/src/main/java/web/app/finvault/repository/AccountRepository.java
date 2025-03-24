package web.app.finvault.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.app.finvault.entity.Account;

public interface AccountRepository extends JpaRepository<Account, String> {



}
