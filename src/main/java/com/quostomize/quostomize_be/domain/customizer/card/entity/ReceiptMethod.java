package com.quostomize.quostomize_be.domain.customizer.card.entity;

import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table (name = "receipt_methods")
public class ReceiptMethod extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receipt_type_id")
    private Long receiptTypeId;

    @Column (name = "method_name", length = 20, nullable = false)
    private String methodName;
}
