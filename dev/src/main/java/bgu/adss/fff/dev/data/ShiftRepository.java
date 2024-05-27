package bgu.adss.fff.dev.data;

import bgu.adss.fff.dev.domain.models.EmbeddedShiftId;
import bgu.adss.fff.dev.domain.models.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShiftRepository extends JpaRepository<Shift, EmbeddedShiftId> {
}
