package edu.miu.common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SyncFailure<Source> {
    private Source source;
    private SyncType syncType;
}
