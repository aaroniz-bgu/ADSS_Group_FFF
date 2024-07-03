package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.BranchRepository;
import bgu.adss.fff.dev.domain.models.Branch;
import bgu.adss.fff.dev.exceptions.BranchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchServiceImpl implements BranchService {
    private final BranchRepository repository;

    @Autowired
    public BranchServiceImpl(BranchRepository repository) {
        this.repository = repository;

        if(!repository.existsById("main")) {
            repository.saveAndFlush(new Branch("main"));
        }
    }

    /**
     * Creates a new branch in the system.
     * @param branch
     * @return The created branch.
     */
    @Override
    public Branch createBranch(Branch branch) {
        if(repository.existsById(branch.getName())) {
            throw BranchException.alreadyExists(branch.getName());
        }

        return repository.saveAndFlush(branch);
    }

    /**
     * Gets a branch by its name.
     * @param name
     * @return The branch with the given name.
     */
    @Override
    public Branch getBranch(String name) {
        return repository.findById(name).orElseThrow(() -> BranchException.notFound(name));
    }

    /**
     * Gets all branches in the system.
     * @return A list of all branches in the system.
     */
    @Override
    public List<Branch> getBranches() {
        return repository.findAll();
    }

    /**
     * Removes a role by its name.
     * @param name
     */
    @Override
    public void removeBranch(String name) {
        if(!repository.existsById(name)) {
            throw BranchException.notFound(name);
        }
        repository.deleteById(name);
    }
}