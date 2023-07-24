package com.enigma.procurementwarehouse.repository;

import com.enigma.procurementwarehouse.entity.SuperAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuperAdminRepository extends JpaRepository<SuperAdmin, String> {
}
