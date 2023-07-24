package com.enigma.procurementwarehouse.service;

import com.enigma.procurementwarehouse.entity.ReportData;
import org.springframework.data.domain.Page;

import java.io.Writer;

public interface ReportDataService {
    ReportData create(ReportData reportData);

    Page<ReportData> getAllOrder(String date, Integer month, Integer year, Integer page, Integer size);

    void writeReportDataToCsv(Writer writer, String date, Integer monthm, Integer year);

}
