package com.enigma.procurementwarehouse.service.impl;

import com.enigma.procurementwarehouse.entity.Vendor;
import com.enigma.procurementwarehouse.model.request.UpdateVendoreRequest;
import com.enigma.procurementwarehouse.model.request.VendorRequest;
import com.enigma.procurementwarehouse.model.response.VendorResponse;
import com.enigma.procurementwarehouse.repository.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendorServiceImplTest {

    @Mock
    private VendorRepository vendorRepository;

    @InjectMocks
    private VendorServiceImpl vendorService;

    private VendorRequest vendorRequest;
    private Vendor vendor;

    @BeforeEach
    void setUp() {
        vendorRequest = VendorRequest.builder()
                .name("VendorName")
                .phone("123456789")
                .address("VendorAddress")
                .build();

        vendor = Vendor.builder()
                .id("1")
                .name("VendorName")
                .phone("123456789")
                .address("VendorAddress")
                .build();
    }

    @Test
    void create_Success() {
        when(vendorRepository.save(any(Vendor.class))).thenReturn(vendor);

        Vendor createdVendor = vendorService.create(vendorRequest);

        assertNotNull(createdVendor);
        assertEquals(vendorRequest.getName(), createdVendor.getName());
        assertEquals(vendorRequest.getPhone(), createdVendor.getPhone());
        assertEquals(vendorRequest.getAddress(), createdVendor.getAddress());

        verify(vendorRepository, times(1)).save(any(Vendor.class));
    }

    @Test
    void create_VendorDataAlreadyUsed_ThrowsResponseStatusException() {
        when(vendorRepository.save(any(Vendor.class)))
                .thenThrow(new DataIntegrityViolationException("Data vendor already used"));

        assertThrows(ResponseStatusException.class, () -> vendorService.create(vendorRequest));

        verify(vendorRepository, times(1)).save(any(Vendor.class));
    }

    @Test
    void getById_Success() {
        when(vendorRepository.findById(vendor.getId())).thenReturn(Optional.of(vendor));

        Vendor foundVendor = vendorService.getById(vendor.getId());

        assertNotNull(foundVendor);
        assertEquals(vendor.getId(), foundVendor.getId());
        assertEquals(vendor.getName(), foundVendor.getName());
        assertEquals(vendor.getPhone(), foundVendor.getPhone());
        assertEquals(vendor.getAddress(), foundVendor.getAddress());

        verify(vendorRepository, times(1)).findById(vendor.getId());
    }

    @Test
    void getById_VendorNotFound_ThrowsResponseStatusException() {
        when(vendorRepository.findById(vendor.getId())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> vendorService.getById(vendor.getId()));

        verify(vendorRepository, times(1)).findById(vendor.getId());
    }


    @Test
    void update_Success() {
        UpdateVendoreRequest updateRequest = UpdateVendoreRequest.builder()
                .vendorId(vendor.getId())
                .vendorName("UpdatedName")
                .phone("987654321")
                .address("UpdatedAddress")
                .build();

        when(vendorRepository.findById(vendor.getId())).thenReturn(Optional.of(vendor));
        when(vendorRepository.save(any(Vendor.class))).thenReturn(vendor);

        VendorResponse updatedVendor = vendorService.update(updateRequest);

        assertNotNull(updatedVendor);
        assertEquals(updateRequest.getVendorId(), updatedVendor.getId());
        assertEquals(updateRequest.getVendorName(), updatedVendor.getName());
        assertEquals(updateRequest.getPhone(), updatedVendor.getPhone());
        assertEquals(updateRequest.getAddress(), updatedVendor.getAddress());

        verify(vendorRepository, times(1)).findById(vendor.getId());
        verify(vendorRepository, times(1)).save(any(Vendor.class));
    }

    @Test
    void update_VendorNotFound_ThrowsResponseStatusException() {
        UpdateVendoreRequest updateRequest = UpdateVendoreRequest.builder()
                .vendorId(vendor.getId())
                .vendorName("UpdatedName")
                .phone("987654321")
                .address("UpdatedAddress")
                .build();

        when(vendorRepository.findById(vendor.getId())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> vendorService.update(updateRequest));

        verify(vendorRepository, times(1)).findById(vendor.getId());
        verify(vendorRepository, never()).save(any(Vendor.class));
    }

    @Test
    void delete_Success() {
        when(vendorRepository.findById(vendor.getId())).thenReturn(Optional.of(vendor));

        vendorService.delete(vendor.getId());

        verify(vendorRepository, times(1)).findById(vendor.getId());
        verify(vendorRepository, times(1)).deleteById(vendor.getId());
    }

    @Test
    void delete_VendorNotFound_ThrowsResponseStatusException() {
        when(vendorRepository.findById(vendor.getId())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> vendorService.delete(vendor.getId()));

        verify(vendorRepository, times(1)).findById(vendor.getId());
        verify(vendorRepository, never()).deleteById(vendor.getId());
    }
}
