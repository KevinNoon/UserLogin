package com.optimised.backup.data.service;

import com.optimised.backup.data.entity.User;
import com.optimised.backup.data.repositories.UserRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepo repository;

    public UserService(UserRepo repository) {
        this.repository = repository;
    }

    public Optional<User> get(Long id) {
        return repository.findById(id);
    }

    public User update(User entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<User> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<User> list(Pageable pageable, Specification<User> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public void save(User user){
        repository.save(user);
    }

    public User findUserByUserName(String username){
        return repository.findByUsername(username);
    }

    public List<User> findAllUsers(){
        return repository.findAll();
    }

}
