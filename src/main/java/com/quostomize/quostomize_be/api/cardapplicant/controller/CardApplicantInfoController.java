package com.quostomize.quostomize_be.api.cardapplicant.controller;

import com.quostomize.quostomize_be.api.cardapplicant.dto.BenefitHierarchyDTO;
import com.quostomize.quostomize_be.api.cardapplicant.dto.CardApplicantDTO;
import com.quostomize.quostomize_be.api.cardapplicant.dto.CardApplicantDetailsDTO;
import com.quostomize.quostomize_be.common.dto.ResponseDTO;
import com.quostomize.quostomize_be.domain.customizer.cardapplication.service.CardApplicantInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/card-applicants")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "카드 신청 API", description = "카드를 신청을 생성하거나 신청 정보 조회 기능 제공")
public class CardApplicantInfoController {

    private final CardApplicantInfoService cardApplicantInfoService;

    @GetMapping("/search-benefit")
    @Operation(summary = "가맹점 정보 조회", description = "페이지 진입 시 모든 상위 분류 와 하위 분류의  가맹점 이름을 조회한다.")
    public ResponseEntity<ResponseDTO> searchBenefit(){
        List<BenefitHierarchyDTO> benefitInformation = cardApplicantInfoService.getBenefitInformation();
        return ResponseEntity.ok(new ResponseDTO<>(benefitInformation));
    }


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
    public ResponseEntity<CardApplicantDetailsDTO> createCardApplicant(@Valid @RequestBody CardApplicantDTO cardApplicantDTO) {
        CardApplicantDetailsDTO result = cardApplicantInfoService.createCardApplicant(cardApplicantDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
