package edu.miu.common.repository;

import edu.miu.common.domain.SyncError;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
@ConditionalOnProperty(value = "common.sync.bootstrap", havingValue = "true")
public interface SyncErrorRepository extends BaseRepository<SyncError, Integer> {

    @Query("SELECT se FROM SyncError se WHERE se.auditData.createdOn BETWEEN :startTime AND :endTime ORDER BY se.auditData.createdOn DESC")
    List<SyncError> findSyncErrorsByCreatedOnBetween(LocalDateTime startTime, LocalDateTime endTime);

}