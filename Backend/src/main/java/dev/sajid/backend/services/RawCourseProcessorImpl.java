package dev.sajid.backend.services;

import dev.sajid.backend.models.normalized.course.*;
import dev.sajid.backend.models.raw.Course;
import dev.sajid.backend.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

// Renamed to @Service as it's a better fit for a business logic component
@Service
public class RawCourseProcessorImpl implements RawCourseProcessor {

    // Final repositories are a good practice with constructor injection
    private final ProgramSubjectRepository programSubjectRepository;
    private final SubjectRepository subjectRepository;
    private final SchemeRepository schemeRepository;
    private final BranchRepository branchRepository;
    private final ProgramRepository programRepository;

    // Records for creating unique keys for Maps, a great approach!
    record ProgramKey(Degree degree, int schemeId, int branchId) {}
    record ProgramSubjectKey(int programId, int subjectId, int semester) {}

    Map<String, Scheme> schemesMap;
    Map<Integer, Branch> branchesMap;
    Map<String, Subject> subjectsMap;
    Map<ProgramKey, Program> programsMap;


    RawCourseProcessorImpl(ProgramRepository programRepository, BranchRepository branchRepository,
            SchemeRepository schemeRepository, SubjectRepository subjectRepository, ProgramSubjectRepository programSubjectRepository) {
        this.programRepository = programRepository;
        this.branchRepository = branchRepository;
        this.schemeRepository = schemeRepository;
        this.subjectRepository = subjectRepository;
        this.programSubjectRepository = programSubjectRepository;
    }

    
    @Override
    @Transactional
    public void processRawCourses(List<Course> rawCourses) {
        // The order of operations is critical
        schemesMap = findOrCreateSchemes(rawCourses);
        branchesMap = findOrCreateBranches(rawCourses);
        subjectsMap = findOrCreateSubjects(rawCourses);
        programsMap = findOrCreatePrograms(rawCourses, schemesMap, branchesMap);
        
        // This is the final step, using all the data we've prepared
        createProgramSubjects(rawCourses, programsMap, subjectsMap);
    }
    
    // Each of these methods will now follow the "fetch, find new, save all, return map" pattern
    @Override
    public Map<String, Scheme> findOrCreateSchemes(List<Course> rawCourses) {
        // 1. Fetch existing schemes into a map for fast lookup
        Map<String, Scheme> existingSchemes = schemeRepository.findAll().stream()
                .collect(Collectors.toMap(Scheme::getCode, Function.identity()));

        // 2. Identify new schemes that are not in the database
        List<Scheme> newSchemes = rawCourses.stream()
                .map(Course::getScheme)
                .distinct()
                .filter(schemeCode -> !existingSchemes.containsKey(schemeCode))
                .map(schemeCode -> new Scheme(0, schemeCode, 0, null)) // Assuming default year
                .toList();

        // 3. Save all new schemes in a single batch operation
        if (!newSchemes.isEmpty()) {
            schemeRepository.saveAll(newSchemes);
            // Add the newly saved schemes back to the map
            newSchemes.forEach(s -> existingSchemes.put(s.getCode(), s));
        }

        return existingSchemes;
    }
    
    // Similar logic for Branches
    public Map<Integer, Branch> findOrCreateBranches(List<Course> rawCourses) {
        Map<Integer, Branch> existingBranches = branchRepository.findAll().stream()
                .collect(Collectors.toMap(Branch::getBranchCode, Function.identity()));
        
        List<Branch> newBranches = rawCourses.stream()
                .map(c -> c.getBranch().charAt(0))
                .distinct()
                .map(c -> Integer.parseInt(c.toString()))
                .filter(branchCode -> !existingBranches.containsKey(branchCode))
                .map(branchCode -> new Branch(0, branchCode,
                        BranchNamer.getBranchShortNameByCode(branchCode),
                        BranchNamer.getBranchFullNameByCode(branchCode)))
                .toList();
        
        if (!newBranches.isEmpty()) {
            branchRepository.saveAll(newBranches);
            newBranches.forEach(b -> existingBranches.put(b.getBranchCode(), b));
        }
        
        return existingBranches;
    }

    // Similar logic for Subjects
    public Map<String, Subject> findOrCreateSubjects(List<Course> rawCourses) {
        Map<String, Subject> existingSubjects = subjectRepository.findAll().stream()
                .collect(Collectors.toMap(Subject::getShortForm, Function.identity()));
        
        List<Subject> newSubjects = rawCourses.stream()
                .map(c -> new Subject(0, c.getSCode(), c.getSubName(),
                        c.getSCode().endsWith("(P)") ? SubjectType.Lab : SubjectType.Theory, null))
                .filter(s -> !existingSubjects.containsKey(s.getShortForm()))
                .distinct() // distinct must be after map for objects
                .toList();

        if (!newSubjects.isEmpty()) {
            subjectRepository.saveAll(newSubjects);
            newSubjects.forEach(s -> existingSubjects.put(s.getShortForm(), s));
        }
        
        return existingSubjects;
    }

    @Override
    public Map<ProgramKey, Program> findOrCreatePrograms(List<Course> rawCourses, Map<String, Scheme> schemesMap, Map<Integer, Branch> branchesMap) {
        Map<ProgramKey, Program> existingPrograms = programRepository.findAll().stream()
                .collect(Collectors.toMap(p -> new ProgramKey(p.getDegree(), p.getScheme().getId(), p.getBranch().getId()), Function.identity()));
        
        List<Program> newPrograms = rawCourses.stream()
                .map(c -> {
                    Degree degree = c.getDegree().equalsIgnoreCase("btech") ? Degree.BTech : Degree.MTech;
                    Scheme scheme = schemesMap.get(c.getScheme());
                    Branch branch = branchesMap.get(Integer.parseInt(String.valueOf(c.getBranch().charAt(0))));
                    return new ProgramKey(degree, scheme.getId(), branch.getId());
                })
                .distinct()
                .filter(key -> !existingPrograms.containsKey(key))
                .map(key -> {
                    Program p = new Program();
                    p.setDegree(key.degree());
                    p.setScheme(schemesMap.values().stream().filter(s -> s.getId() == key.schemeId()).findFirst().get());
                    p.setBranch(branchesMap.get(key.branchId()));
                    return p;
                })
                .toList();

        if (!newPrograms.isEmpty()) {
            programRepository.saveAll(newPrograms);
            newPrograms.forEach(p -> existingPrograms.put(new ProgramKey(p.getDegree(), p.getScheme().getId(), p.getBranch().getId()), p));
        }

        return existingPrograms;
    }
    
    // The final, corrected linking step
    @Override
    public void createProgramSubjects(List<Course> rawCourses, Map<ProgramKey, Program> programsMap, Map<String, Subject> subjectsMap) {
        Set<ProgramSubjectKey> existingLinks = programSubjectRepository.findAll().stream()
            .map(ps -> new ProgramSubjectKey(ps.getProgram().getId(), ps.getSubject().getId(), ps.getSemester()))
            .collect(Collectors.toSet());
        
        List<ProgramSubject> newLinks = rawCourses.stream()
            .map(c -> {
                // Find the parent Program and Subject from the maps we prepared
                Degree degree = c.getDegree().equalsIgnoreCase("btech") ? Degree.BTech : Degree.MTech;
                int schemeId = schemesMap.values().stream().filter(s -> s.getCode().equals(c.getScheme())).findFirst().get().getId();
                int branchId = branchesMap.values().stream().filter(b -> b.getBranchCode() == Integer.parseInt(String.valueOf(c.getBranch().charAt(0)))).findFirst().get().getId();
                
                Program program = programsMap.get(new ProgramKey(degree, schemeId, branchId));
                Subject subject = subjectsMap.get(c.getSCode());
                
                // This check is crucial
                if (program == null || subject == null) return null;

                // Create the new link only if it doesn't already exist
                ProgramSubjectKey key = new ProgramSubjectKey(program.getId(), subject.getId(), c.getSem());
                if(existingLinks.contains(key)) return null;

                ProgramSubject ps = new ProgramSubject();
                ps.setProgram(program);
                ps.setSubject(subject);
                ps.setSemester(c.getSem());
                return ps;
            })
            .filter(ps -> ps != null) // Filter out nulls from failed lookups or existing links
            .distinct()
            .toList();

        if (!newLinks.isEmpty()) {
            programSubjectRepository.saveAll(newLinks);
        }
    }
}