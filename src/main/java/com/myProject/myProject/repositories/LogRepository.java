package com.myProject.myProject.repositories;

import com.myProject.myProject.model.MyLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<MyLog, Integer> {

}
