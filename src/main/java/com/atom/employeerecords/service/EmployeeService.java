package com.atom.employeerecords.service;

import com.atom.employeerecords.dto.EmployeeDto;
import com.atom.employeerecords.model.Employee;
import com.atom.employeerecords.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    private EmployeeDto toDto(Employee e) {
        return new EmployeeDto(e.getId(), e.getFirstName(), e.getLastName(), e.getEmail());
    }

    private Employee toEntity(EmployeeDto d) {
        Employee e = new Employee();
        e.setId(d.getId());
        e.setFirstName(d.getFirstName());
        e.setLastName(d.getLastName());
        e.setEmail(d.getEmail());
        return e;
    }

    public EmployeeDto create(EmployeeDto dto) {
        // ensure a new entity is created regardless of any id provided by client
        Employee entity = toEntity(dto);
        entity.setId(null);
        Employee saved = repository.save(entity);
        return toDto(saved);
    }

    public List<EmployeeDto> findAll() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public Optional<EmployeeDto> findById(String id) {
        return repository.findById(id).map(this::toDto);
    }

    public Optional<EmployeeDto> update(String id, EmployeeDto dto) {
        return repository.findById(id).map(existing -> {
            existing.setFirstName(dto.getFirstName());
            existing.setLastName(dto.getLastName());
            existing.setEmail(dto.getEmail());
            return toDto(repository.save(existing));
        });
    }

    public boolean delete(String id) {
        return repository.findById(id).map(e -> {
            repository.deleteById(id);
            return true;
        }).orElse(false);
    }
}
