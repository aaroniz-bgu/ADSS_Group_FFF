package bgu.adss.fff.dev.data;

import bgu.adss.fff.dev.domain.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    @Query("SELECT r FROM role r WHERE r.name IN :names")
    List<Role> findByNameIn(@Param("names") Collection<String> names);
}