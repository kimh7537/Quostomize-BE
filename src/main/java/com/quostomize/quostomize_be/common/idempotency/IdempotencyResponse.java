package com.quostomize.quostomize_be.common.idempotency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class IdempotencyResponse implements Serializable {

    private ProcessStatus processStatus;
    private Object result;

    public boolean isProcessing() {
        return this.processStatus == ProcessStatus.PROCESSING;
    }

    public boolean isSucceed() {
        return this.processStatus == ProcessStatus.SUCCESS;
    }

    public boolean isFailed() {return this.processStatus == ProcessStatus.FAILED;}
}
