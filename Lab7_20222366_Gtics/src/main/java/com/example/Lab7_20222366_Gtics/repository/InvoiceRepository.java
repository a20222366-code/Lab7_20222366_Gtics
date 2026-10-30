package com.example.Lab7_20222366_Gtics.repository;

import com.example.Lab7_20222366_Gtics.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
}
