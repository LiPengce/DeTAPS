package com.lpc.controller;

import com.lpc.pojo.NotarialRecord;
import com.lpc.pojo.Result;
import com.lpc.service.NotaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/notary")
@CrossOrigin
public class NotaryController {
    @Autowired
    private NotaryService notaryService;

    @PostMapping("/trace")
    public Result trace(@RequestParam("kaggStr") MultipartFile kaggStr,@RequestParam("uskStr") MultipartFile uskStr) throws Exception {

        return notaryService.trace(kaggStr,uskStr);
    }

    @GetMapping("/record")
    public List<NotarialRecord>getRecords(){

        return notaryService.getRecords();
    }

}
