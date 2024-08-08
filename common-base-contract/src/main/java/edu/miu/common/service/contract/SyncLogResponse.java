package edu.miu.common.service.contract;

import lombok.Data;

import java.io.Serializable;

@Data
public class SyncLogResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private SyncEventResponse syncEvent;

    private SyncTypeResponse syncType;

    private String sourceValue;

    private String targetValue;

    private CommonAuditInfo auditInfo = new CommonAuditInfo();

}
