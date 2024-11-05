package com.quostomize.quostomize_be.domain.customizer.card.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table (name = "terms")
public class Term {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "term_title", nullable = false)
    String termTitle;

    @Column (name = "term_context", nullable = false)
    String termContext;

    @Column (name = "is_essential", nullable = false)
    String isEssential;

    @Column (name = "is_agree", nullable = false)
    String isAgree;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;
}
