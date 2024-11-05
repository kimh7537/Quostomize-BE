package com.quostomize.quostomize_be.domain.customizer.card.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table (name = "receipt_methods")
public class ReceiptMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "method_name", nullable = false)
    private String methodName;
}
