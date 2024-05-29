package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.domain.models.Branch;

import java.util.List;

public interface BranchService {
    Branch createBranch(Branch branch);
    Branch getBranch(String name);
    List<Branch> getBranches();
    void removeBranch(String name);
}
