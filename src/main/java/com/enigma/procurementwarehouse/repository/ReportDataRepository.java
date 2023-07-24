package com.enigma.procurementwarehouse.repository;

import com.enigma.procurementwarehouse.entity.ReportData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ReportDataRepository extends JpaRepository<ReportData, String>, JpaSpecificationExecutor<ReportData> {
    List<ReportData> findByDate(Timestamp date);

}
