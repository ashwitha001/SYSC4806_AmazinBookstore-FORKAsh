package repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.Checkout;

@Repository
public interface CheckoutRepository extends JpaRepository<Checkout, Long>{
    
}
