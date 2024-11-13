package com.quostomize.quostomize_be.api.cardapplicant;

import com.quostomize.quostomize_be.common.DTO.ResponseDTO;
import com.quostomize.quostomize_be.domain.customizer.cardApplication.service.CardApplicantInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/card-applicants")
@RequiredArgsConstructor
public class CardApplicantInfoController {

    private final CardApplicantInfoService cardApplicantInfoService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<CardApplicantDetailsDTO>>> getCardApplicantsList() {
        return ResponseEntity.ok(cardApplicantInfoService.getCardApplicantsList());
    }

    @GetMapping("/{cardApplicantId}")
    public ResponseEntity<ResponseDTO<CardApplicantDetailsDTO>> getCardApplicantsDetails(@PathVariable String cardApplicantId) {
        return ResponseEntity.ok(cardApplicantInfoService.getCardApplicantsDetails(cardApplicantId));
    }


    @PostMapping("/api/cardApplicants")
    public ResponseEntity<Void> createCardApplicant(@RequestBody CardApplicantDTO cardApplicantDTO) {
        return ResponseEntity.created(URI.create("/api/cardApplicants/"+cardApplicantInfoService.createCardApplicant(cardApplicantDTO))).build();
    }
}
