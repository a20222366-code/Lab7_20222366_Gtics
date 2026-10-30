package com.example.Lab7_20222366_Gtics.repository;

import com.example.Lab7_20222366_Gtics.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Integer id);
}