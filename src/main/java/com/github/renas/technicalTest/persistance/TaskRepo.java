package com.github.renas.technicalTest.persistance;

import com.github.renas.technicalTest.controller.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;


@Repository
public interface TaskRepo extends JpaRepository<Task, UUID> {}
