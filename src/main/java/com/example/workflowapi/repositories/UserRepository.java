package com.example.workflowapi.repositories;

import com.example.workflowapi.model.WorkflowUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<WorkflowUser, Long> {
    Optional<WorkflowUser> findByUsername(String username);
}
