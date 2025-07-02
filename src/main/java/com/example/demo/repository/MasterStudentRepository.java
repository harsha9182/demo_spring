package com.example.demo.repository;

import com.example.demo.table.MasterStudent;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MasterStudentRepository extends JpaRepository<MasterStudent,String> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM  master_student d WHERE d.uuid IN (:ids)", nativeQuery = true)
    void deleteAllByIds(@Param("ids") List<String> ids);


    @Transactional
    @Modifying
    @Query(value = "UPDATE master_student SET status = :status WHERE uuid IN :uuids", nativeQuery = true)
    void updateStatusForUuids(@Param("uuids") List<String> uuids, @Param("status") int status);
}
