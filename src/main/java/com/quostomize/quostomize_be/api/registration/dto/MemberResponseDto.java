package com.quostomize.quostomize_be.api.registration.dto;

public record MemberResponseDto(
        String memberLoginId,
        String name,
        String email,
        String memberPhoneNumber
) {}
