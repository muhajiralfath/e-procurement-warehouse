package com.enigma.procurementwarehouse.repository;

import com.enigma.procurementwarehouse.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, String> {
}
