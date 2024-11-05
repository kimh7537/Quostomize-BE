package com.quostomize.quostomize_be.domain.customizer.benefit.entity;

import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "franchises")
public class Franchise extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "franchise_id")
    private Long franchiseId;

    @Column(name = "franchise_name", length = 20, nullable = false)
    private String franchiseName;
}
