package bgu.adss.fff.dev.data;

import bgu.adss.fff.dev.domain.models.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
