package com.enigma.procurementwarehouse.repository;

import com.enigma.procurementwarehouse.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
    Optional<Product> findTopByOrderByProductCodeDesc();
    Optional<Product> findByIdAndIsDeletedFalse(String id);
}
