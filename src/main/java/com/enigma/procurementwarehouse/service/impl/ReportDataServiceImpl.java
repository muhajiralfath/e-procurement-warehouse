package com.enigma.procurementwarehouse.service.impl;

import com.enigma.procurementwarehouse.entity.ReportData;
import com.enigma.procurementwarehouse.repository.ReportDataRepository;
import com.enigma.procurementwarehouse.service.ReportDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
}
