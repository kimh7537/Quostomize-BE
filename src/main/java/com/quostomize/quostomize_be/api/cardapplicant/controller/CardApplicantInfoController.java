package com.quostomize.quostomize_be.api.cardapplicant.controller;

import com.quostomize.quostomize_be.api.cardapplicant.dto.CardApplicantDTO;
import com.quostomize.quostomize_be.api.cardapplicant.dto.CardApplicantDetailsDTO;
import com.quostomize.quostomize_be.domain.customizer.cardapplication.service.CardApplicantInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/card-applicants")
@RequiredArgsConstructor
@Tag(name = "카드 신청 API", description = "카드를 신청을 생성하거나 정보를 조회하는 API입니다.")
public class CardApplicantInfoController {

    private final CardApplicantInfoService cardApplicantInfoService;

    @GetMapping
    @Operation(summary = "모든 신청 내역을 조회", description = "사용자 관계 없이 모든 카드 신청 내역을 조회하는 api입니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = ResponseStatus.class),
            examples = {@ExampleObject(
                    name = "response example",
                    value = "{\n" +
                            "  \"data\": [\n" +
                            "    {\n" +
                            "      \"residenceNumber\": \"991231-1234567\",\n" +
                            "      \"applicantName\": \"김철수\",\n" +
                            "      \"englishName\": \"KIM CHEOLSU\",\n" +
                            "      \"zipCode\": \"03048\",\n" +
                            "      \"shippingAddress\": \"서울 종로구 청와대로 1\",\n" +
                            "      \"shippingDetailAddress\": \"청와대 검문소\",\n" +
                            "      \"applicantEmail\": \"testmember01@testmail.com\",\n" +
                            "      \"phoneNumber\": \"010-1234-5678\",\n" +
                            "      \"homeAddress\": \"서울 종로구 청와대로 1\",\n" +
                            "      \"homeDetailAddress\": \"청와대 검문소\"\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}"
            )}
        )
    )
    public ResponseEntity<ResponseDTO<List<CardApplicantDetailsDTO>>> getCardApplicantsList() {
        return ResponseEntity.ok(new ResponseDTO<List<CardApplicantDetailsDTO>>(cardApplicantInfoService.getCardApplicantsList()));
    }


    @GetMapping("/{cardApplicantId}")
    @Operation(summary = "단일 신청 내역을 조회", description = "카드 신청 내역 id를 통해 특정 신청 내역을 조회하는 api입니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = ResponseStatus.class),
                    examples = {@ExampleObject(
                            name = "reseponse example",
                            value = "{\n" +
                                    "  \"data\": \n" +
                                    "    {\n" +
                                    "      \"residenceNumber\": \"991231-1234567\",\n" +
                                    "      \"applicantName\": \"김철수\",\n" +
                                    "      \"englishName\": \"KIM CHEOLSU\",\n" +
                                    "      \"zipCode\": \"03048\",\n" +
                                    "      \"shippingAddress\": \"서울 종로구 청와대로 1\",\n" +
                                    "      \"shippingDetailAddress\": \"청와대 검문소\",\n" +
                                    "      \"applicantEmail\": \"testmember01@testmail.com\",\n" +
                                    "      \"phoneNumber\": \"010-1234-5678\",\n" +
                                    "      \"homeAddress\": \"서울 종로구 청와대로 1\",\n" +
                                    "      \"homeDetailAddress\": \"청와대 검문소\"\n" +
                                    "    }\n" +
                                    "}"
                    )}
            )
    )
    public ResponseEntity<ResponseDTO<CardApplicantDetailsDTO>> getCardApplicantsDetails(@PathVariable Long cardApplicantId) {
        return ResponseEntity.ok(new ResponseDTO<CardApplicantDetailsDTO>(cardApplicantInfoService.getCardApplicantsDetails(cardApplicantId)));
    }


    @PostMapping
    @Operation(summary = "카드 신청", description = "필요한 정보를 통해 카드 생성을 신청하는 api입니다.")
    @ApiResponse(responseCode = "201", description = "카드 신청 성공",
            content = @Content(schema = @Schema(implementation = ResponseStatus.class),
                    examples = {@ExampleObject(
                            name = "response example",
                            value = "{}"
                    )}
            )
    )
    public ResponseEntity<Void> createCardApplicant(@RequestBody CardApplicantDTO cardApplicantDTO) {
        return ResponseEntity.created(URI.create("/api/cardApplicants/"+cardApplicantInfoService.createCardApplicant(cardApplicantDTO))).build();
    }
}
