package com.quostomize.quostomize_be.config;

import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = CustomSecurityContextFactory.class)
public @interface MockUser {
    
        String memberName() default "박기합";

        String memberLoginId() default "gihab1234";

        String memberPassword() default "abc1234@";

        String memberEmail() default "gihab@naver.com";

        String residenceNumber() default "9904041111111";

        String zipCode() default "12345";

        String memberAddress() default "서울특별시 종로구 14";

        String memberDetailAddress() default "안알랴줌";

        String memberPhoneNumber() default "01055556666";

        String secodaryAuthCode() default "123456";

}
