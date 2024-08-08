package edu.miu.common.service.contract;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SyncEventResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String sourceName;

    private String targetName;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private Integer sourceCount = 0;

    private Integer createCount = 0;

    private Integer updateCount = 0;

    private Integer deleteCount = 0;

    private String duration;

    private CommonAuditInfo auditInfo = new CommonAuditInfo();

}
