package com.quostomize.quostomize_be.common.email.service;

import com.quostomize.quostomize_be.common.config.RedisConfig;
import com.quostomize.quostomize_be.common.error.ErrorCode;
import com.quostomize.quostomize_be.common.error.exception.AppException;
import com.quostomize.quostomize_be.domain.customizer.cardapplication.repository.CardApplicantInfoRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailSendService {

    private static final int MAX_RETRIES = 3; //최대 재시도 횟수

    @Value("${adminEmail}")
    private String ADMIN_EMAIL;

    @Value("${spring.mail.username}")
    private String serviceName;

    private final JavaMailSender javaMailSender;
    private final RedisConfig redisConfig;
    private final CardApplicantInfoRepository cardApplicantInfoRepository;
    @Qualifier("taskExecutor")
    private final ThreadPoolTaskExecutor taskExecutor;

    // 현재 인증된 관리자의 이메일을 가져오는 메서드
    public String getCurrentAdminId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        } else {
            throw new RuntimeException("현재 인증된 관리자가 없습니다.");
        }
    }

    public void adminMailSend(String title, MultipartFile htmlFile, Integer optionalTerms, String adminId) {
        try{
            String htmlContent = new String(htmlFile.getBytes(), StandardCharsets.UTF_8);
            List<String> emails = (optionalTerms == null || optionalTerms == -1)
                    ? cardApplicantInfoRepository.findAllEmails()
                    : cardApplicantInfoRepository.findEmailsByOptionalTerms(optionalTerms);
            emails.forEach(email -> CompletableFuture.runAsync(() ->
                            sendEmailWithRetry(title, email, htmlContent), taskExecutor)
                            .exceptionally(throwable ->  handleEmailFailure(email, throwable))
            );
        }catch (Exception e){
            handleGeneralFailure("Bulk Email Processing", e);
        }
    }

    /**
     * 개별 이메일 실패 처리
     */
    private Void handleEmailFailure(String email, Throwable throwable) {
        String errorMessage = throwable.getMessage();
        log.error("Email sending failed for {}: {}", email, errorMessage, throwable);
        notifyAdminForFailure(email, errorMessage);
        return null;
    }

    /**
     * 일반적인 실패 처리
     */
    private void handleGeneralFailure(String context, Exception e) {
        String errorMessage = e.getMessage();
        log.error("Error during {}: {}", context, errorMessage, e);
        notifyAdminForFailure(context, errorMessage);
        throw new AppException(ErrorCode.EMAIL_SEND_FAIL);
    }

    private boolean sendEmailWithRetry(String title, String recipientEmail, String htmlContent) {
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                sendEmail(title, recipientEmail, htmlContent);
                log.info("Email sent successfully to {}", recipientEmail);
                return true; // 성공
            } catch (Exception e) {
                log.warn("Attempt {} to send email to {} failed: {}", attempt, recipientEmail, e.getMessage());
            }
        }
        // 재시도 끝난 후에도 실패한 경우 false 반환
        return false;
    }

    // 개별 이메일 전송 메서드
    private void sendEmail(String title, String recipientEmail, String htmlContent) throws MessagingException{
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        helper.setFrom(serviceName); // 발신자
        helper.setTo(recipientEmail); // 수신자
        helper.setSubject(title); // 제목
        helper.setText(htmlContent, true); // HTML 본문
        javaMailSender.send(message);
        log.info("Email sent successfully");
    }

    private void notifyAdminForFailure(String recipientEmail, String errorMessage){
        try{
            String subject = "Email Sending Failed : " + recipientEmail;
            String content = String.format("메일 발송 에러가 발생함 %s.\n\nError details:\n%s", recipientEmail, errorMessage);
            sendEmail(subject, ADMIN_EMAIL, content);
        }catch (Exception e) {
            throw new AppException(ErrorCode.EMAIL_SEND_FAIL);
        }
    }


    /* 랜덤 인증번호 생성 */
    public int  makeRandomNum() {
        Random r = new Random();
        StringBuilder randomNumber = new StringBuilder();
        for(int i = 0; i < 6; i++) {
            randomNumber.append(r.nextInt(10));
        }
        return Integer.parseInt(randomNumber.toString());
    }

    /* 이메일 전송 */
    public void mailSend(String setFrom, String toMail, String title, String content, int authNumber) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");
            helper.setFrom(setFrom); // service name
            helper.setTo(toMail); // customer email
            helper.setSubject(title); // email title
            helper.setText(content,true); // content, html: true
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // 에러 출력
        }
        // redis에 3분 동안 이메일과 인증 코드 저장
        ValueOperations<String, String> valOperations = redisConfig.redisTemplate().opsForValue();
        valOperations.set(toMail, Integer.toString(authNumber), 180, TimeUnit.SECONDS);
    }

    /* 이메일 작성 */
    public String joinEmail(String email) {
        int authNumber = makeRandomNum();
        String customerMail = email;
        String title = "인증을 위한 이메일입니다";
        String content =
                "<b>안녕하세요!</b> \uD83D\uDC4B" +
                        "<br><br>" +
                        "이메일 인증을 완료하기 위해 아래 절차를 진행해 주세요:" +
                        "<br><br>" +
                        "<b>1. 인증 번호 입력</b> \uD83D\uDCDD" +
                        "<br>" +
                        "인증 번호 : " + "<b>" + authNumber + "</b>" +
                        "<br><br>" +
                        "2. 학교 인증 칸에 해당 번호를 입력해 주세요. \uD83D\uDD11" +
                        "<br><br>" +
                        "인증 절차가 완료되면, 추가적인 안내를 드리겠습니다." +
                        "<br><br>" +
                        "감사합니다! \uD83D\uDE0A";
        mailSend(serviceName, customerMail, title, content, authNumber);
        return Integer.toString(authNumber);
    }

    @Transactional
    /* 인증번호 확인 및 이메일 도메인 검증 */
    public Boolean checkAuthNum(String email, String authNum) {
        // Redis에서 인증번호 확인
        ValueOperations<String, String> valOperations = redisConfig.redisTemplate().opsForValue();
        String code = valOperations.get(email);
        boolean isAuthSuccessful = Objects.equals(code, authNum);
        if(!isAuthSuccessful){
            throw new AppException(ErrorCode.EMAIL_CERTIFICATION_ERROR);
        }
        return isAuthSuccessful;
    }


}
