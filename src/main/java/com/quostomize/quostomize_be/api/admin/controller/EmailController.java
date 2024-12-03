package com.quostomize.quostomize_be.api.admin.controller;

import com.quostomize.quostomize_be.api.admin.dto.AdminEmailRequest;
import com.quostomize.quostomize_be.api.admin.dto.EmailRequest;
import com.quostomize.quostomize_be.api.admin.dto.EmailResponse;
import com.quostomize.quostomize_be.common.dto.ResponseDTO;
import com.quostomize.quostomize_be.common.email.service.EmailSendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "이메일 인증", description = "이메일 인증 관련 API")
@RestController
@RequestMapping("/v1/api/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailSendService emailSendService;

    @PostMapping(value = "/send/admin", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "회원 메일 발송", description = "관리자가 회원들에게 메일을 발송 합니다. (-1:모두 발송)(1~3: 선택지에 따라 발송)")
    public ResponseEntity<Void> sendEmailWIthHtmlFile(@ModelAttribute AdminEmailRequest adminEmailRequest){
        emailSendService.adminMailSend(adminEmailRequest.title(), adminEmailRequest.htmlFile(), adminEmailRequest.optionalTerms());
        return ResponseEntity.ok().build();
    }

    /* Send Email: 인증번호 전송 버튼 click */
    @PostMapping("/send")
    @Operation(summary = "이메일 인증 요청(프로토타입)", description = "이메일 주소를 통해 이메일 인증을 요청합니다")
    public ResponseEntity<ResponseDTO> mailSend(@RequestBody @Valid EmailRequest emailRequestDto) {
        String code = emailSendService.joinEmail(emailRequestDto.email());
        // response를 JSON 문자열으로 반환
        Map<String, String> response = new HashMap<>();
        response.put("code", code);
        return ResponseEntity.ok(new ResponseDTO<>(response));
    }

    /* Email Auth: 인증번호 입력 후 인증 버튼 click */
    @PostMapping("/send/emailAuth")
    @Operation(summary = "이메일 인증 인증번호 확인(프로토타입)", description = "이메일 주소로 받은 인증 번호와 이메일 주소를 통해 인증을 완료합니다")
    public ResponseEntity<ResponseDTO> authCheck( @RequestBody @Valid EmailResponse emailResponse) {
        Boolean checked = emailSendService.checkAuthNum(emailResponse.email(), emailResponse.authNum());
        return ResponseEntity.ok(new ResponseDTO<>("인증성공"));
    }

}
