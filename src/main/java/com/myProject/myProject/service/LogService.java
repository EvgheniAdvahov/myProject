package com.myProject.myProject.service;

import com.myProject.myProject.model.MyLog;
import com.myProject.myProject.repositories.LogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    public void saveLogToDb(MyLog myLog){
        logRepository.save(myLog);
    }

}
