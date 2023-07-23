package com.enigma.procurementwarehouse.service.impl;

import com.enigma.procurementwarehouse.entity.Vendor;
import com.enigma.procurementwarehouse.model.request.UpdateVendoreRequest;
import com.enigma.procurementwarehouse.model.request.VendorRequest;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;

    @Override
    public Vendor create(VendorRequest vendor) {
        try {
            Vendor saveVendor = Vendor.builder()
                    .name(vendor.getName())
                    .phone(vendor.getPhone())
                    .address(vendor.getAddress())
                    .build();
            return vendorRepository.save(saveVendor);
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
    public Page<VendorResponse> getAllVendor(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Vendor> vendors = vendorRepository.findAll(pageable);

        List<Vendor> vendorList = new ArrayList<>(vendors.getContent());
        List<VendorResponse> vendorResponseList = vendorList.stream().map(vendor -> VendorResponse.builder()
                .id(vendor.getId())
                .name(vendor.getName())
                .address(vendor.getAddress())
                .phone(vendor.getPhone())
                .build()).collect(Collectors.toList());

        return new PageImpl<>(vendorResponseList, pageable, vendors.getTotalElements());
    }

    @Override
    public VendorResponse update(UpdateVendoreRequest request) {
        Vendor vendorFound = getById(request.getVendorId());

        Vendor vendor = Vendor.builder()
                .id(request.getVendorId())
                .name(request.getVendorName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .createdBy(vendorFound.getCreatedBy())
                .createdAt(vendorFound.getCreatedAt())
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

}
