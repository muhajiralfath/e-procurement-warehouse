package com.enigma.procurementwarehouse.service.impl;

import com.enigma.procurementwarehouse.entity.ReportData;
import com.enigma.procurementwarehouse.repository.ReportDataRepository;
import com.enigma.procurementwarehouse.service.ReportDataService;
import com.enigma.procurementwarehouse.specification.ReportDataSepecMonthly;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportDataServiceImpl implements ReportDataService {
    public final ReportDataRepository reportDataRepository;
    @Override
    public ReportData create(ReportData reportData) {
        try {
            return reportDataRepository.saveAndFlush(reportData);
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data vendor already used");
        }
    }

    @Override
    public Page<ReportData> getAllOrder(String date, Integer month, Integer year, Integer page, Integer size) {
        Specification<ReportData> specification = ReportDataSepecMonthly.getSpecification(date, month, year);
        Pageable  pageable = PageRequest.of(page, size);
        Page<ReportData> reportData = reportDataRepository.findAll(specification, pageable);

        List<ReportData> reportData1 = reportData.getContent();

        return new PageImpl<>(reportData1, pageable, reportData.getTotalElements());
    }


    @Override
    public void writeReportDataToCsv(Writer writer, String date, Integer month, Integer year) {

        List<ReportData> reportDataList = reportDataRepository.findAll();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            csvPrinter.printRecord("KODE BARANG", "TANGGAL", "NAMA VENDOR","NAMA BARANG","KATEGORI", "HARGA BARANG", "QTY", "JUMLAH");
            for (ReportData data : reportDataList) {
                csvPrinter.printRecord(data.getProductCode(), data.getDate(), data.getVendorName(), data.getProductName(), data.getCategory(), data.getPrice(), data.getQuantity(), data.getTotalPrice());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }



}
