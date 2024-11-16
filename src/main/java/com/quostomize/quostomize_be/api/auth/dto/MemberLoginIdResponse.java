package com.quostomize.quostomize_be.api.auth.dto;

public record MemberLoginIdResponse(
        String memberLoginId
) {
    public static MemberLoginIdResponse from(String memberLoginId) {
        return new MemberLoginIdResponse(memberLoginId);
    }
}
