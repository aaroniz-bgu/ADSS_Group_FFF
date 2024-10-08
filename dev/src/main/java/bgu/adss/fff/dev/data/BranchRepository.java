package bgu.adss.fff.dev.data;

import bgu.adss.fff.dev.domain.models.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<Branch, String> {

}
