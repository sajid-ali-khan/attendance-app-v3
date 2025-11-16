package dev.sajid.backend.services;

import dev.sajid.backend.dtos.BranchDto;
import dev.sajid.backend.models.normalized.course.Branch;
import dev.sajid.backend.repositories.StudentBatchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchService {
    private final StudentBatchRepository studentBatchRepository;

    public BranchService(StudentBatchRepository studentBatchRepository) {
        this.studentBatchRepository = studentBatchRepository;
    }

    public List<BranchDto> getDistinctBranchesByBranchCodes(){
        List<Integer> branchCodes = studentBatchRepository.findDistinctBranchCodes();

        return branchCodes.stream().map(bc -> {
            String shortForm = BranchNamer.getBranchShortNameByCode(bc);
            String fullForm = BranchNamer.getBranchFullNameByCode(bc);
            return new BranchDto(bc, shortForm, fullForm);
        }).toList();
    }
}
