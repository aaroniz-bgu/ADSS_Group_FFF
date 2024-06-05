package bgu.adss.fff.dev.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemConfigRepository extends JpaRepository<ConfigurationPair, String> {
}
