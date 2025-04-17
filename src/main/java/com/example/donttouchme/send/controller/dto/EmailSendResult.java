package com.example.donttouchme.send.controller.dto;

public record EmailSendResult(
        String message,
        int successCnt,
        int failCnt
) {
}
