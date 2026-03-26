package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.entities.Student;
import edu.unimagdalena.lms.repositories.StudentRepository;
import edu.unimagdalena.lms.services.mapper.StudentMapper;
import jakarta.persistence.EntityExistsException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import edu.unimagdalena.lms.api.dto.StudentDtos.*;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {
    private final StudentRepository repo;
    private final StudentMapper mapper;

    @Override
    @Transactional
    public StudentResponse create(StudentCreate dto) {
        Student studentEntity = mapper.toEntity(dto);
        studentEntity.setCreatedAt(Instant.now());
        studentEntity.setUpdatedAt(Instant.now());
        var entitySaved = repo.save(studentEntity);
        return mapper.toResponse(entitySaved);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getById(Long id){
        return repo.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new EntityExistsException("Student %d not found".formatted(id)));
    }

    @Override @Transactional(readOnly = true)
    public List<StudentResponse>  getAll(){
        return repo.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override @Transactional
    public StudentResponse update(Long id, StudentUpdate dto) {
        Student studentFound = repo.findById(id)
                .orElseThrow(() -> new EntityExistsException("Student %d not found".formatted(id)));

        mapper.patch(studentFound, dto);
        studentFound.setUpdatedAt(Instant.now());
        return mapper.toResponse(repo.save(studentFound));
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

}
