package com.enigma.procurementwarehouse.service;

import com.enigma.procurementwarehouse.entity.Vendor;
import com.enigma.procurementwarehouse.model.request.UpdateVendoreRequest;
import com.enigma.procurementwarehouse.model.response.VendorResponse;
import org.springframework.data.domain.Page;

public interface VendorService {

    Vendor create(Vendor vendor);
    Vendor getById(String id);
    Page<Vendor> getAllVendor(Integer page, Integer size);
    VendorResponse update(UpdateVendoreRequest request);
    void softDelete(String id);
    void delete(String id);

}
