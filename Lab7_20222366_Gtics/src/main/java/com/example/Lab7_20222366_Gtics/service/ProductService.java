package com.example.Lab7_20222366_Gtics.service;


import com.example.Lab7_20222366_Gtics.entity.Product;
import com.example.Lab7_20222366_Gtics.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;


    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public boolean nombreDuplicado(String name, Integer id) {
        if (id == null) {

            return productRepository.existsByName(name);
        }


        return productRepository.existsByNameAndIdNot(name, id);
    }


    public Product guardar(Product product) {
        return productRepository.save(product);
    }


}