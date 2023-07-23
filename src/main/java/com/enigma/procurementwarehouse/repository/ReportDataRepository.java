package com.enigma.procurementwarehouse.repository;

import com.enigma.procurementwarehouse.entity.ReportData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportDataRepository extends JpaRepository<ReportData, String> {
}
