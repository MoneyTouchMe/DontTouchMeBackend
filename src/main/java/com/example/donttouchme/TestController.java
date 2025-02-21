package com.example.donttouchme;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@Tag(name = "TEST", description = "TEST")
public class TestController {

    @GetMapping
    @Operation(
            summary = "TEST",
            description = "TEST"
    )
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("TEST SUCCESS");
    }
}
