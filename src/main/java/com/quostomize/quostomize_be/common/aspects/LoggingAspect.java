package com.quostomize.quostomize_be.common.aspects;

import com.quostomize.quostomize_be.domain.customizer.cardapplication.repository.CardApplicantInfoRepository;
import com.quostomize.quostomize_be.domain.log.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.quostomize.quostomize_be.domain.log.enums.LogStatus.FAILURE;
import static com.quostomize.quostomize_be.domain.log.enums.LogStatus.SUCCESS;
import static com.quostomize.quostomize_be.domain.log.enums.LogType.MAIL_FAILURE;
import static com.quostomize.quostomize_be.domain.log.enums.LogType.MAIL_SEND;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LoggingAspect {

    private final LogService logService;
    private final CardApplicantInfoRepository cardApplicantInfoRepository;

    /**
     * Before: 대상 메서드가 실행되기 전에 Advice를 실행
     *
     * @param joinPoint
     */
    @Before("execution(* com.quostomize.quostomize_be.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Before: " + joinPoint.getSignature().getName());
    }

    /**
     * After : 대상 메서드가 실행된 후에 Advice를 실행
     *
     * @param joinPoint
     */
    @After("execution(* com.quostomize.quostomize_be.*.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        log.info("After: " + joinPoint.getSignature().getName());
    }


    /**
     * AfterReturning: 대상 메서드가 정상적으로 실행되고 반환된 후에 Advice를 실행
     *
     * @param joinPoint
     * @param result
     */
    @AfterReturning(pointcut = "execution(* com.quostomize.quostomize_be.*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("AfterReturning: " + joinPoint.getSignature().getName() + " result: " + result);
    }

    /**
     * AfterThrowing: 대상 메서드에서 예외가 발생했을 때 Advice를 실행
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "execution(* com.quostomize.quostomize_be.*.*(..))", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.info(
                "AfterThrowing: " + joinPoint.getSignature().getName() + " exception: " + e.getMessage());
    }

    /**
     * Around : 대상 메서드 실행 전, 후 또는 예외 발생 시에 Advice를 실행
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.quostomize.quostomize_be.*.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Around before: " + joinPoint.getSignature().getName());
        Object result = joinPoint.proceed();
        log.info("Around after: " + joinPoint.getSignature().getName());
        return result;
    }

    // 관리자 이메일 발송에 대한 로깅
    @After("execution(* com.quostomize.quostomize_be.common.email.service.EmailSendService.adminMailSend(..)) && args(title, htmlFile, optionalTerms, adminId)")
    public void logAdminMailSend(String title, MultipartFile htmlFile, Integer optionalTerms, String adminId) {
        List<String> emails = (optionalTerms == null || optionalTerms == -1)
                ? cardApplicantInfoRepository.findAllEmails()
                : cardApplicantInfoRepository.findEmailsByOptionalTerms(optionalTerms);

        for (String email : emails) {
            logService.saveLog(MAIL_SEND, "회원 이메일: " + email + "에게 메일 발송 완료", Long.parseLong(adminId), SUCCESS, "/v1/api/admin/email");
        }
    }

    /**
     * 메일 발송 실패 시 Advice
     * 이메일 전송 실패 시에 예외 정보를 로깅하고 LogService를 통해 저장합니다.
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "execution(* com.quostomize.quostomize_be.common.email.service.EmailSendService.sendEmailWithRetry(..))", throwing = "e")
    public void logEmailSendFailure(JoinPoint joinPoint, Throwable e) {
        Object[] args = joinPoint.getArgs();
        String recipientEmail = (String) args[1]; // 두 번째 파라미터가 수신자 이메일
        String errorMessage = e.getMessage();

        logService.saveLog(MAIL_FAILURE, "메일 발송 실패 - 수신자: " + recipientEmail + ", 에러 메시지: " + errorMessage, null, FAILURE, "/v1/api/admin/email");
    }
}
