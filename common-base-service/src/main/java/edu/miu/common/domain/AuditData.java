package edu.miu.common.domain;

import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;

@Data
@Embeddable
public class AuditData {

    @Column(name = "CreatedBy", insertable = false, updatable = false)
    private String createdBy;

    @Column(name = "UpdatedBy", insertable = false, updatable = false)
    private String updatedBy;

    @Column(name = "CreatedOn", insertable = false, updatable = false)
    private LocalDateTime createdOn;

    @Column(name = "UpdatedOn", insertable = false, updatable = false)
    private LocalDateTime updatedOn;

    @Column(name = "CreatedByAppUser", updatable = false)
    private String createdByAppUser;

    @Column(name = "UpdatedByAppUser")
    private String updatedByAppUser;

}
