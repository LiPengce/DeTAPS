package com.lpc.controller;

import com.lpc.pojo.Result;
import com.lpc.service.SignerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/signer")
@CrossOrigin
public class SignerController {
    @Autowired
    private SignerService signerService;

    @PostMapping("/sign")
    public Result sign(MultipartFile skstr,MultipartFile m, MultipartFile pids, MultipartFile gid) throws Exception {

        return signerService.Sign(skstr, m, pids, gid);
    }
}
