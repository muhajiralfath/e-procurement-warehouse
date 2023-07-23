package com.enigma.procurementwarehouse.service.impl;

import com.enigma.procurementwarehouse.entity.Vendor;
import com.enigma.procurementwarehouse.model.request.UpdateVendoreRequest;
import com.enigma.procurementwarehouse.model.response.VendorResponse;
import com.enigma.procurementwarehouse.repository.VendorRepository;
import com.enigma.procurementwarehouse.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;

    @Override
    public Vendor create(Vendor vendor) {
        try {
            return vendorRepository.saveAndFlush(vendor);
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data vendor already used");
        }
    }

    @Override
    public Vendor getById(String id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor not found"));
    }

    @Override
    public Page<Vendor> getAllVendor(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Vendor> vendors = vendorRepository.findAll(pageable);
        List<Vendor> vendorList = new ArrayList<>(vendors.getContent());

        return new PageImpl<>(vendorList, pageable, vendors.getTotalElements());
    }

    @Override
    public VendorResponse update(UpdateVendoreRequest request) {
        getById(request.getVendorId());

        Vendor vendor = Vendor.builder()
                .id(request.getVendorId())
                .name(request.getVendorName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .build();

        vendorRepository.save(vendor);

        return VendorResponse.builder()
                .id(vendor.getId())
                .name(vendor.getName())
                .phone(vendor.getPhone())
                .address(vendor.getAddress())
                .build();
    }

    @Override
    public void delete(String id) {
        getById(id);
        vendorRepository.deleteById(id);
    }

    @Override
    public void softDelete(String id) {
        try {
            Vendor vendor = getById(id);
            vendor.setIsDelete(true);
            vendorRepository.save(vendor);
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Data vendor not found");
        }

    }
}
