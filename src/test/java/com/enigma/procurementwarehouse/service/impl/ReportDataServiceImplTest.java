package com.enigma.procurementwarehouse.service.impl;

import com.enigma.procurementwarehouse.entity.ReportData;
import com.enigma.procurementwarehouse.model.response.ReportDataResponse;
import com.enigma.procurementwarehouse.repository.ReportDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportDataServiceImplTest {

    @Mock
    private ReportDataRepository reportDataRepository;

    @InjectMocks
    private ReportDataServiceImpl reportDataService;

    private ReportData reportData;

    @BeforeEach
    void setUp() {
        reportData = ReportData.builder()
                .id("1")
                .productCode("P001")
                .date(LocalDate.now().atStartOfDay())
                .vendorName("Vendor 1")
                .productName("Product 1")
                .category("Category 1")
                .price((long) 100.0)
                .quantity(10)
                .totalPrice((long) 1000.0)
                .build();
    }

    @Test
    void create_Success() {
        when(reportDataRepository.saveAndFlush(any(ReportData.class))).thenReturn(reportData);

        ReportData createdReportData = reportDataService.create(reportData);

        assertNotNull(createdReportData);
        assertEquals(reportData.getId(), createdReportData.getId());
        assertEquals(reportData.getProductCode(), createdReportData.getProductCode());
        assertEquals(reportData.getDate(), createdReportData.getDate());
        assertEquals(reportData.getVendorName(), createdReportData.getVendorName());
        assertEquals(reportData.getProductName(), createdReportData.getProductName());
        assertEquals(reportData.getCategory(), createdReportData.getCategory());
        assertEquals(reportData.getPrice(), createdReportData.getPrice());
        assertEquals(reportData.getQuantity(), createdReportData.getQuantity());
        assertEquals(reportData.getTotalPrice(), createdReportData.getTotalPrice());

        verify(reportDataRepository, times(1)).saveAndFlush(any(ReportData.class));
    }

    @Test
    void create_ReportDataAlreadyExists_ThrowsResponseStatusException() {
        when(reportDataRepository.saveAndFlush(any(ReportData.class)))
                .thenThrow(new DataIntegrityViolationException("Data vendor already used"));

        assertThrows(ResponseStatusException.class, () -> reportDataService.create(reportData));

        verify(reportDataRepository, times(1)).saveAndFlush(any(ReportData.class));
    }

    @Test
    void getAllOrder_Success() {
        List<ReportData> reportDataList = new ArrayList<>();
        reportDataList.add(reportData);

        Pageable pageable = Pageable.unpaged();
        Page<ReportData> reportDataPage = new PageImpl<>(reportDataList, pageable, reportDataList.size());

        when(reportDataRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(reportDataPage);

        Page<ReportData> resultPage = reportDataService.getAllOrder("2023-07-23", 7, 2023, 0, 10);

        assertFalse(resultPage.isEmpty());
        assertEquals(reportDataList.size(), resultPage.getContent().size());
        assertEquals(reportDataPage.getTotalElements(), resultPage.getTotalElements());

        verify(reportDataRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void writeReportDataToCsv_Success() throws IOException {
        List<ReportData> reportDataList = new ArrayList<>();
        reportDataList.add(reportData);

        StringWriter stringWriter = new StringWriter();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<ReportDataResponse> dataResponseList = new ArrayList<>();
        for (ReportData reportData : reportDataList) {
            ReportDataResponse response = ReportDataResponse.builder()
                    .id(reportData.getId())
                    .productCode(reportData.getProductCode())
                    .date(reportData.getDate().format(formatter))
                    .vendorName(reportData.getVendorName())
                    .productName(reportData.getProductName())
                    .category(reportData.getCategory())
                    .price(reportData.getPrice())
                    .quantity(reportData.getQuantity())
                    .totalPrice(reportData.getTotalPrice())
                    .build();
            dataResponseList.add(response);
        }

        reportDataService.writeReportDataToCsv(stringWriter, "2023-07-23", 7, 2023);

        String expectedCsv = "KODE BARANG,TANGGAL,NAMA VENDOR,NAMA BARANG,KATEGORI,HARGA BARANG,QTY,JUMLAH\r\n" +
                "P001,23/07/2023,Vendor 1,23/07/2023,Category 1,100.0,10,1000.0\r\n";


    }
}
