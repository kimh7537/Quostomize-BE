package com.quostomize.quostomize_be.domain.customizer.card.entity;

import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "terms")
public class Term extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "term_id")
    private Long termId;

    @Column(name = "term_title", nullable = false)
    private String termTitle;

    @Column(name = "term_context", columnDefinition = "TEXT", nullable = false)
    private String termContext;

    @Column(name = "is_essential", nullable = false)
    private Boolean isEssential;

    @Column(name = "is_agree", nullable = false)
    private Boolean isAgree;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;
}
