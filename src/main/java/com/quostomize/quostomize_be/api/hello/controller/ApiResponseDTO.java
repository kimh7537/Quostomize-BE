package com.quostomize.quostomize_be.api.hello.controller;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ApiResponseDTO {
    private String ctxAreaFk100;
    private String ctxAreaNk100;
    private List<Output1DTO> output1;
    private List<Output2DTO> output2;
    private String rtCd;
    private String msgCd;
    private String msg1;
}