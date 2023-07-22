package com.enigma.procurementwarehouse.repository;

import com.enigma.procurementwarehouse.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
}
