package com.sultanov.demo.controller;

import com.sultanov.demo.dto.TransferRequest;
import com.sultanov.demo.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/transfer")
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<String> transferMoney(@RequestBody TransferRequest transferRequest) {
        transferService.transferMoney(transferRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Money transfer completed successfully");
    }
}
