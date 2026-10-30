package com.example.Lab7_20222366_Gtics.repository;

import com.example.Lab7_20222366_Gtics.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    boolean existsByDocument(String document);
    boolean existsByDocumentAndIdNot(String document, Integer id);

}
