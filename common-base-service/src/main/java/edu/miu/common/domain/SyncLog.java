package edu.miu.common.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Data;

/**
 * <h1>Maharishi International University<br/>Computer Science Department</h1>
 * 
 * <p>Domain entity. Simple POJO.</p>
 *
 * @author Payman Salek
 * 
 * @version 1.2.0
 * @since 1.2.0
 * 
 */

@Data
@Entity
@Table(name = "[Synchronization].[dbo].[SyncLog]")
public class SyncLog implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "SyncEventId")
	private SyncEvent syncEvent;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "SyncType", nullable = false)
	private SyncType syncType;
	
	@Column(name = "SourceValue", length = 9000)
	private String sourceValue;
	
	@Column(name = "targetValue", length = 9000)
	private String targetValue;
	
	@Embedded
	private AuditInfo auditInfo = new AuditInfo();
	
	public static SyncLog generateSyncLogEntry(SyncEvent event, SyncType syncType) {
		SyncLog logEntry = new SyncLog();
		
		logEntry.setSyncEvent(event);
		logEntry.setSyncType(syncType);
		
		return logEntry;
	}

}
