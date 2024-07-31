package com.lpc.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lpc
 * @version 1.0
 * @since 2024-04-13
 */
@Data
public class TestRequestBody {
    MultipartFile file;
    int t;
}
