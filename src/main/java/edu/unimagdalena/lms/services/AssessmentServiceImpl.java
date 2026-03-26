package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.AssessmentDtos.*;
import edu.unimagdalena.lms.entities.Assessment;
import edu.unimagdalena.lms.entities.Course;
import edu.unimagdalena.lms.entities.Student;
import edu.unimagdalena.lms.repositories.AssessmentRepository;
import edu.unimagdalena.lms.repositories.CourseRepository;
import edu.unimagdalena.lms.repositories.StudentRepository;
import edu.unimagdalena.lms.services.mapper.AssessmentMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AssessmentServiceImpl implements AssessmentService {
    private final AssessmentRepository repo;
    private final StudentRepository studentRepo;
    private final CourseRepository courseRepo;
    private final AssessmentMapper mapper;

    @Override
    public AssessmentResponse create(AssessmentRequestCreate dto) {
        Assessment assessment = mapper.toEntity(dto);

        Student student = studentRepo.findById(dto.studentId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Student %d not found".formatted(dto.studentId())
                ));

        Course course = courseRepo.findById(dto.courseId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Course %d not found".formatted(dto.courseId())
                ));

        assessment.setStudent(student);
        assessment.setCourse(course);

        return mapper.toResponse(repo.save(assessment));
    }

    @Override
    @Transactional(readOnly = true)
    public AssessmentResponse getById(Long id) {
        return repo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Assessment %d not found".formatted(id)
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentResponse> findAll() {
        return repo.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public AssessmentResponse update(Long id, AssessmentRequestUpdate dto) {
        Assessment assessment = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Assessment %d not found".formatted(id)
                ));

        mapper.patch(assessment, dto);
        return mapper.toResponse(repo.save(assessment));
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentResponse> findByType(String type) {
        return repo.findByType(type).stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentResponse> findByScoreGreaterThanEqual(int score) {
        return repo.findByScoreGreaterThanEqual(score).stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentResponse> findByStudentId(Long studentId) {
        return repo.findAssessmentByStudentId(studentId).stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentResponse> findByCourseId(long courseId) {
        return repo.findAssessmentByCourseId(courseId).stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentResponse> findByStudentIdAndCourseId(long studentId, long courseId) {
        return repo.findAssessmentByStudentIdAndCourseId(studentId, courseId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Double findAverageScoreByCourseId(Long courseId) {
        return repo.findAverageScoreByCourseId(courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentResponse> findTop10ByCourseIdOrderByScoreDesc(Long courseId) {
        return repo.findTop10ByCourseIdOrderByScoreDesc(courseId).stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByCourseIdAndScoreGreaterThanEqual(long courseId, int minScore) {
        return repo.countByCourseIdAndScoreGreaterThanEqual(courseId, minScore);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentResponse> findByStudentEmail(String email) {
        return repo.findByStudent_Email(email).stream().map(mapper::toResponse).toList();
    }
}

