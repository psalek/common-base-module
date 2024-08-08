package edu.miu.common.repository;

import java.util.List;

import edu.miu.common.domain.SyncType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.miu.common.domain.SyncLog;

@Repository
@Transactional
@ConditionalOnProperty(value = "common.sync.bootstrap", havingValue = "true")
public interface SyncLogRepository extends BaseRepository<SyncLog, Integer> {

    List<SyncLog> findAllBySyncEventId(Integer syncEventId);

    Page<SyncLog> findAllBySyncEventId(Integer syncEventId, Pageable pageable);

    @Query("SELECT DISTINCT sl.syncType FROM SyncLog sl WHERE sl.syncEvent.id = :syncEventId")
    List<SyncType> findDistinctSyncTypesBySyncEventId(Integer syncEventId);

    Page<SyncLog> findAllBySyncEventIdAndSyncType(Integer syncEventId, SyncType syncType, Pageable pageable);

}
