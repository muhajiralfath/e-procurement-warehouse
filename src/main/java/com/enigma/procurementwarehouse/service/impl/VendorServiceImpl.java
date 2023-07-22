package com.enigma.procurementwarehouse.service.impl;

import com.enigma.procurementwarehouse.entity.Vendor;
import com.enigma.procurementwarehouse.model.request.UpdateVendoreRequest;
import com.enigma.procurementwarehouse.model.response.VendorResponse;
import com.enigma.procurementwarehouse.repository.VendorRepository;
import com.enigma.procurementwarehouse.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;

    @Override
    public Vendor save(Vendor vendor) {
        return null;
    }

    @Override
    public Vendor getById(String id) {
        return null;
    }

    @Override
    public Page<Vendor> getAllVendor() {
        return null;
    }

    @Override
    public VendorResponse update(UpdateVendoreRequest request) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public void softdelete(String id) {

    }
}
