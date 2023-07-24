package com.enigma.procurementwarehouse.controller;

import com.enigma.procurementwarehouse.service.ReportDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/report")
public class ReportDataController {

    private final ReportDataService reportDataService;


    @GetMapping
    public ResponseEntity<?> getReportData(
            HttpServletResponse servletResponse,
            @RequestParam(name = "date", required = false, defaultValue = "") String date,
            @RequestParam(name = "month", required = false) Integer month,
            @RequestParam(name = "year", required = false) Integer year
    ) throws IOException {
        servletResponse.setContentType("text/csv");
        reportDataService.writeReportDataToCsv(servletResponse.getWriter(), date, month, year);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report.csv\"")
                .build();
    }
}
