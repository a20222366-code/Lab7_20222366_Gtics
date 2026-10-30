package com.example.Lab7_20222366_Gtics.repository;




import com.example.Lab7_20222366_Gtics.entity.InvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, Integer> {


    List<InvoiceDetail> findByInvoiceId(Integer invoiceId);

}