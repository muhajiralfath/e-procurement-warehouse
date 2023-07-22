package com.enigma.procurementwarehouse.repository;

import com.enigma.procurementwarehouse.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, String> {
}
