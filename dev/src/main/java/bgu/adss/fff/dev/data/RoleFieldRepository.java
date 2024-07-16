package bgu.adss.fff.dev.data;

import bgu.adss.fff.dev.domain.models.RoleField;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleFieldRepository extends JpaRepository<RoleField, RoleField.RoleFieldKey> {
}
