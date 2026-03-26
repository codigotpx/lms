package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.EnrollmentDtos.*;
import edu.unimagdalena.lms.entities.Course;
import edu.unimagdalena.lms.entities.Enrollment;
import edu.unimagdalena.lms.entities.Student;
import edu.unimagdalena.lms.repositories.CourseRepository;
import edu.unimagdalena.lms.repositories.EnrollmentRepository;
import edu.unimagdalena.lms.repositories.StudentRepository;
import edu.unimagdalena.lms.services.mapper.EnrollmentMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository repo;
    private final StudentRepository studentRepo;
    private final CourseRepository courseRepo;
    private final EnrollmentMapper mapper;

    @Override
    public EnrollmentResponse create(EnrollmentCreate dto) {
        Enrollment enrollment = mapper.toEntity(dto);

        Student student = studentRepo.findById(dto.studentId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Student %d not found".formatted(dto.studentId())
                ));

        Course course = courseRepo.findById(dto.courseId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Course %d not found".formatted(dto.courseId())
                ));

        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrolledAt(Instant.now());

        return mapper.toResponse(repo.save(enrollment));
    }

    @Override
    @Transactional(readOnly = true)
    public EnrollmentResponse getById(Long id) {
        return repo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Enrollment %d not found".formatted(id)
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> findAll() {
        return repo.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public EnrollmentResponse update(Long id, EnrollmentUpdate dto) {
        Enrollment enrollment = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Enrollment %d not found".formatted(id)
                ));

        mapper.patch(enrollment, dto);
        return mapper.toResponse(repo.save(enrollment));
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> findByCourseId(Long courseId) {
        return repo.findByCourseId(courseId).stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> findByStudentId(Long studentId) {
        return repo.findByStudentId(studentId).stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> findByStatus(String status) {
        return repo.findByStatus(status).stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByCourseId(Long courseId) {
        return repo.countByCourseId(courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByStudentIdAndCourseIdAndStatus(Long studentId, Long courseId, String status) {
        return repo.existsByStudentIdAndCourseIdAndStatus(studentId, courseId, status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> findByCourseIdOrderByEnrolledAtDesc(Long courseId) {
        return repo.findByCourseIdOrderByEnrolledAtDesc(courseId).stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByStatus(String status) {
        return repo.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> findByStudentIdAndCourseId(Long studentId, Long courseId) {
        return repo.findByStudentIdAndCourseId(studentId, courseId).stream().map(mapper::toResponse).toList();
    }
}

