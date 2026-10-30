package com.example.Lab7_20222366_Gtics.service;


import com.example.Lab7_20222366_Gtics.entity.Customer;
import com.example.Lab7_20222366_Gtics.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;


    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    public boolean documentoDuplicado(String document, Integer id) {


        if (id == null) {


            return customerRepository.existsByDocument(document);
        }


        return customerRepository.existsByDocumentAndIdNot(document, id);
    }



    public boolean documentoValidoParaTipo(String document, String documentType) {


        if (document == null || documentType == null) {
            return false;
        }


        if ("DNI".equals(documentType)) {
            return document.matches("[0-9]{8}");
        }

        if ("RUC".equals(documentType)) {
            return document.matches("[0-9]{11}");
        }


        return false;
    }


    public Customer guardar(Customer customer) {
        return customerRepository.save(customer);
    }


}