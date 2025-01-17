package com.quostomize.quostomize_be.domain.customizer.adminResponse.entity;

import com.quostomize.quostomize_be.common.entity.BaseTimeEntity;
import com.quostomize.quostomize_be.domain.customizer.memberQuestion.entity.MemberQuestion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "admin_responses")
public class AdminResponse extends BaseTimeEntity {

    public AdminResponse() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "response_sequence_id")
    private Long responseSequenceId;

    @Lob
    @Column(name = "response_content",columnDefinition = "TEXT", nullable = false)
    private String responseContent;

    @OneToOne
    @JoinColumn(name = "questions_sequence_id", nullable = false)
    private MemberQuestion memberQuestion;

    @Builder
    public AdminResponse(Long responseSequenceId, String responseContent, MemberQuestion memberQuestion) {
        this.responseSequenceId = responseSequenceId;
        this.responseContent = responseContent;
        this.memberQuestion = memberQuestion;
    }

    public String getMemberLoginId() {
        return this.memberQuestion.getMember().getMemberLoginId();
    }
}
