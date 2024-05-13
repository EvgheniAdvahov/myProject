package com.myProject.myProject.repositories;

import com.myProject.myProject.model.MyLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogRepository extends JpaRepository<MyLog, Integer> {

    List<MyLog> findAllByItemIdOrderByCreatedAtDesc(int itemId);
}
