package dev.sajid.backend.services;

import dev.sajid.backend.models.normalized.course.Branch;
import dev.sajid.backend.repositories.StudentBatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BranchService {
    @Autowired
    private StudentBatchRepository studentBatchRepository;

    public List<Branch> getDistinctBranches(){

        return studentBatchRepository.findDistinctBranches();
    }
}
