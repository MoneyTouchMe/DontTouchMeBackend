package com.example.donttouchme.member.controller.dto;

public record TempPasswordIssueResponse(
        String email,
        String message
) {
}
