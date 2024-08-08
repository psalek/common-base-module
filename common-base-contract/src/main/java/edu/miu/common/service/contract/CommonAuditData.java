package edu.miu.common.service.contract;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CommonAuditData implements Serializable {

    private static final long serialVersionUID = 1L;

    private String createdBy;

    private String updatedBy;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    private String createdByAppUser;

    private String updatedByAppUser;

}
