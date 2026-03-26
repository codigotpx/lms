package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.LessonDtos.*;
import edu.unimagdalena.lms.entities.Course;
import edu.unimagdalena.lms.entities.Lesson;
import edu.unimagdalena.lms.repositories.CourseRepository;
import edu.unimagdalena.lms.repositories.LessonRepository;
import edu.unimagdalena.lms.services.mapper.LessonMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LessonServiceImpl implements LessonService {
    private final LessonRepository repo;
    private final CourseRepository courseRepo;
    private final LessonMapper mapper;

    @Override
    public LessonResponse create(LessonCreate dto) {
        Lesson lesson = mapper.toEntity(dto);

        Course course = courseRepo.findById(dto.courseId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Course %d not found".formatted(dto.courseId())
                ));

        lesson.setCourse(course);
        return mapper.toResponse(repo.save(lesson));
    }

    @Override
    @Transactional(readOnly = true)
    public LessonResponse getById(Long id) {
        return repo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Lesson %d not found".formatted(id)
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonResponse> findAll() {
        return repo.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public LessonResponse update(Long id, LessonUpdate dto) {
        Lesson lesson = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Lesson %d not found".formatted(id)
                ));

        mapper.patch(lesson, dto);
        return mapper.toResponse(repo.save(lesson));
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonResponse> findByCourseIdOrderByOrderIndexAsc(Long courseId) {
        return repo.findByCourseIdOrderByOrderIndexAsc(courseId).stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonResponse> findByCourseIdAndTitleContainingIgnoreCase(Long courseId, String title) {
        return repo.findByCourseIdAndTitleContainingIgnoreCase(courseId, title).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByCourseId(Long courseId) {
        return repo.countByCourseId(courseId);
    }
}

