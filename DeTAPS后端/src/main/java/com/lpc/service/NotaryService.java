package com.lpc.service;

import com.lpc.pojo.NotarialRecord;
import com.lpc.pojo.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NotaryService {

    Result trace(MultipartFile kaggStr, MultipartFile uskStr) throws Exception;

    List<NotarialRecord> getRecords();
}
