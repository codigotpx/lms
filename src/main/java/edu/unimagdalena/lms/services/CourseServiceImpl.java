package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.CourseDtos.*;
import edu.unimagdalena.lms.entities.Course;
import edu.unimagdalena.lms.entities.Instructor;
import edu.unimagdalena.lms.repositories.CourseRepository;
import edu.unimagdalena.lms.repositories.InstructorRepository;
import edu.unimagdalena.lms.services.mapper.CourseMapper;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {
    private final CourseMapper mapper;
    private final CourseRepository repo;
    private final InstructorRepository instructorRepo;

    @Override
    public CourseResponse create(CourseRequestCreate dto) {
        Course course = mapper.toEntity(dto);

        Instructor instructor = instructorRepo.findById(dto.instructorId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Instructor %d not found".formatted(dto.instructorId())
                ));
        course.setInstructor(instructor);
        course.setCreatedAt(Instant.now());
        course.setUpdatedAt(Instant.now());
        var entitySaved = repo.save(course);
        return mapper.toResponse(entitySaved);
    }

    @Override @Transactional(readOnly = true)
    public CourseResponse getById(Long id) {
        return repo.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new EntityExistsException("Course %d not found".formatted(id)));
    }

    @Override @Transactional(readOnly = true)
    public List<CourseResponse> findAll() {
        return repo.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override @Transactional
    public CourseResponse update(Long id, CourseRequestUpdate dto) {
        Optional<Course> courseFound = Optional.of(repo.findById(id)
                .orElseThrow(() -> new EntityExistsException("Course %d not found".formatted(id))));

        mapper.patch(courseFound.get(), dto);
        courseFound.get().setUpdatedAt(Instant.now());
        return mapper.toResponse(repo.save(courseFound.get()));
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override @Transactional(readOnly = true)
    public List<CourseResponse> findByStatus(String status) {
        return repo.findByStatus(status).stream().map(mapper::toResponse).toList();
    }

    @Override
    public List<CourseResponse> findByActiveTrue() {
        return repo.findByActiveTrue().stream().map(mapper::toResponse).toList();
    }

    @Override
    public List<CourseResponse> findByInstructorId(Long instructorId) {
        return repo.findByInstructorId(instructorId).stream().map(mapper::toResponse).toList();
    }

    @Override
    public long countByInstructorId(Long instructorId) {
        return repo.countByInstructorId(instructorId);
    }

    @Override
    public List<CourseResponse> findByTitleContaining(String title) {
        return repo.findByTitleContainingIgnoreCaseAndActiveTrue(title).stream().map(mapper::toResponse).toList();
    }
}
