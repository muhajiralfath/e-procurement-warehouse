package com.enigma.procurementwarehouse.service;

import com.enigma.procurementwarehouse.entity.Vendor;
import com.enigma.procurementwarehouse.model.request.UpdateVendoreRequest;
import com.enigma.procurementwarehouse.model.request.VendorRequest;
import com.enigma.procurementwarehouse.model.response.VendorResponse;
import org.springframework.data.domain.Page;

public interface VendorService {

    Vendor create(VendorRequest vendor);
    Vendor getById(String id);
    Page<VendorResponse> getAllVendor(Integer page, Integer size);
    VendorResponse update(UpdateVendoreRequest request);
    void delete(String id);

}
