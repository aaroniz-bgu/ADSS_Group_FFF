package bgu.adss.fff.dev.data;

import bgu.adss.fff.dev.domain.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> { }
