package com.example.donttouchme.send.controller.dto;

public record SMSSendResult(
        String message,
        int successCnt,
        int failCnt
) {
}
