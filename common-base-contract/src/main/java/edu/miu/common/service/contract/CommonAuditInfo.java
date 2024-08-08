package edu.miu.common.service.contract;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CommonAuditInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDateTime creationDate; // DB automatically populates this through trigger

    private String creationUser; // DB automatically populates this through trigger

    private String creationAppUser; // App user, should be populated before INSERT

    private LocalDateTime lastUpdateDate; // DB automatically populates this through trigger

    private String lastUpdateUser; // DB automatically populates this through trigger

    private String lastUpdateAppUser; // App user, should be populated before every UPDATE

}
