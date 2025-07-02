package com.example.demo.repository;


import com.example.demo.table.MasterStation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MasterStationRepository extends JpaRepository<MasterStation,String> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE master_station SET status = :status WHERE uuid IN :uuids", nativeQuery = true)
    void updateStatusForUuids(@Param("uuids") List<String> uuids, @Param("status") int status);

    @Query(value = "SELECT name FROM master_station ms WHERE ms.uuid=?1 ",nativeQuery = true)
    String getStationName(String stationId);

    @Query(value = "SELECT DISTINCT s.name FROM master_station s where status=1 ",nativeQuery = true)
    List<String> findDistinctStationIgnoreCase();


    @Query(value = "SELECT * FROM master_station ms WHERE LOWER(ms.name) = LOWER(:name)",nativeQuery = true)
    MasterStation findByNameIgnoreCase(@Param("name") String name);

    MasterStation findByName(String name);
    @Query(value = "SELECT count(*) FROM master_station",nativeQuery = true)
    String getTotalStationsCount();

    @Query(value = "SELECT * FROM master_station ORDER BY master_station.name",nativeQuery = true)
    List<MasterStation> findAllOrderByName();
}
