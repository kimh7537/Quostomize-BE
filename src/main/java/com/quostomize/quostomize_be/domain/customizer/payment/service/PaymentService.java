package com.quostomize.quostomize_be.domain.customizer.payment.service;

import com.quostomize.quostomize_be.api.payment.dto.PaymentRequestDto;
import com.quostomize.quostomize_be.api.payment.dto.PaymentResponseDto;
import com.quostomize.quostomize_be.api.payment.dto.Product;
import com.quostomize.quostomize_be.domain.customizer.card.entity.Card;
import com.quostomize.quostomize_be.domain.customizer.payment.entity.TodayPayment;
import com.quostomize.quostomize_be.domain.customizer.payment.repository.PaymentRepository;
import com.quostomize.quostomize_be.domain.customizer.payment.entity.Payment;
import com.quostomize.quostomize_be.domain.customizer.payment.repository.TodayPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final TodayPaymentRepository todayPaymentRepository;
    private final CardRepository cardRepository;

    public PaymentResponseDto payment(PaymentRequestDto paymentRequestDto) {
        String cardNumber = paymentRequestDto.card_number();
        Card card = cardRepository.findByCardNumber(cardNumber);
        long totalPrice = 0L;
        Product[] products = paymentRequestDto.products();
        for (int i=0; i<products.length; i++) {
            totalPrice += Long.parseLong(products[i].totalPrice());
        }


        TodayPayment payment = todayPaymentRepository.save(
                TodayPayment.builder()
                .card(card)
                .businessRegistrationNumber(paymentRequestDto.businessRegistrationNumber())
                .products(Arrays.toString(paymentRequestDto.products()))
                .build()
        );



        return PaymentResponseDto.builder()
                .payment_id(payment.getPaymentId())
                .card_number(cardNumber)
                .businessRegistrationNumber(payment.getBusinessRegistrationNumber())
                .totalPrice(String.format("%d",totalPrice))
                .date(payment.getCreatedAt())
                .build();
    }
}
