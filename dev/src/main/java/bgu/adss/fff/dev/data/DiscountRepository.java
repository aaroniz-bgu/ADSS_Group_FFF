package bgu.adss.fff.dev.data;

import bgu.adss.fff.dev.domain.models.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount, Long> { }