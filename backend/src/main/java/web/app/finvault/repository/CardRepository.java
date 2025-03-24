package web.app.finvault.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.app.finvault.entity.Card;

public interface CardRepository extends JpaRepository<Card, String> {

}
