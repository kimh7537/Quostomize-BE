package com.quostomize.quostomize_be.api.auth.dto;

public record ReissueResponse(
        String accessToken
) {
    public static ReissueResponse from(String accessToken) {
        return new ReissueResponse(accessToken);
    }
}
