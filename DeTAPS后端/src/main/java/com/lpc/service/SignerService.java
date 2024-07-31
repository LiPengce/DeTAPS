package com.lpc.service;

import com.lpc.pojo.Result;
import org.springframework.web.multipart.MultipartFile;

public interface SignerService {
    Result Sign(MultipartFile skstr, MultipartFile m, MultipartFile pids, MultipartFile gid) throws Exception;
}
