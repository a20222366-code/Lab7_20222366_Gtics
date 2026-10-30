package com.example.Lab7_20222366_Gtics.service;

import com.example.Lab7_20222366_Gtics.entity.Customer;
import com.example.Lab7_20222366_Gtics.entity.Invoice;
import com.example.Lab7_20222366_Gtics.entity.InvoiceDetail;
import com.example.Lab7_20222366_Gtics.entity.Product;
import com.example.Lab7_20222366_Gtics.repository.CustomerRepository;
import com.example.Lab7_20222366_Gtics.repository.InvoiceDetailRepository;
import com.example.Lab7_20222366_Gtics.repository.InvoiceRepository;
import com.example.Lab7_20222366_Gtics.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public InvoiceService(
            InvoiceRepository invoiceRepository,
            InvoiceDetailRepository invoiceDetailRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceDetailRepository = invoiceDetailRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }



    public Customer buscarCliente(Integer customerId) {
        return customerRepository.findById(customerId).orElseThrow(() -> new IllegalArgumentException("El cliente no existe"));
    }

    public Product buscarProducto(Integer productId) {
        return productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("El producto no existe"));
    }



    public void validarTipoDocumento(String type, Customer customer) {


        if ("Factura".equals(type) && !"RUC".equals(customer.getDocumentType())) {
            throw new IllegalArgumentException("Para emitir una factura, el cliente debe tener RUC");
        }

        if ("Boleta".equals(type)
                && !"DNI".equals(customer.getDocumentType())) {
            throw new IllegalArgumentException(
                    "Para emitir una boleta, el cliente debe tener DNI");
        }



        if (!"Factura".equals(type) && !"Boleta".equals(type)) {
            throw new IllegalArgumentException(
                    "El tipo de comprobante debe ser Factura o Boleta");
        }

    }



    public void validarCantidad(Product product, Integer quantity) {

        if (quantity == null || quantity < 1) {

            throw new IllegalArgumentException(
                    "La cantidad debe ser como mínimo 1");
        }


        if (quantity > product.getStock()) {


            throw new IllegalArgumentException("Stock insuficiente para el producto: " + product.getName());
        }
    }


    public Double calcularSubtotal(Integer quantity, Double price) {
        return quantity * price;
    }





    @Transactional
    public Invoice guardarComprobante(InvoiceForm form) {


        validarDetalles(form);
        validarProductosRepetidos(form);


        Customer customer = buscarCliente(form.getCustomerId());
        validarTipoDocumento(form.getType(), customer);


        for (var detalleForm : form.getDetails()) {

            if (!detalleForm.isSelected()) {
                continue;
            }

            Product product = buscarProducto(detalleForm.getProductId());
            validarCantidad(product, detalleForm.getQuantity());
        }

        Invoice invoice = new Invoice();
        invoice.setType(form.getType());
        invoice.setDate(form.getDate());
        invoice.setCustomer(customer);

        invoice = invoiceRepository.save(invoice);

        for (var detalleForm : form.getDetails()) {

            if (!detalleForm.isSelected()) {
                continue;
            }

            Product product = buscarProducto(detalleForm.getProductId());

            Double subtotal = calcularSubtotal(
                    detalleForm.getQuantity(),
                    product.getPrice()
            );

            InvoiceDetail detalle = new InvoiceDetail();
            detalle.setInvoice(invoice);
            detalle.setProduct(product);
            detalle.setQuantity(detalleForm.getQuantity());
            detalle.setPrice(product.getPrice());
            detalle.setSubtotal(subtotal);

            invoiceDetailRepository.save(detalle);


            product.setStock(
                    product.getStock() - detalleForm.getQuantity()
            );

            productRepository.save(product);
        }

        return invoice;
    }


    public void validarDetalles(InvoiceForm form) {

        if (form.getDetails() == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar al menos un producto");
        }

        boolean haySeleccionado = false;

        for (var detalle : form.getDetails()) {

            if (detalle.isSelected()) {
                haySeleccionado = true;

                if (detalle.getProductId() == null) {
                    throw new IllegalArgumentException(
                            "Debe seleccionar un producto válido");
                }

                if (detalle.getQuantity() == null) {
                    throw new IllegalArgumentException(
                            "La cantidad es obligatoria");
                }

                if (detalle.getQuantity() < 1) {
                    throw new IllegalArgumentException(
                            "La cantidad debe ser como mínimo 1");
                }
            }
        }

        if (!haySeleccionado) {
            throw new IllegalArgumentException(
                    "Debe seleccionar al menos un producto");
        }
    }


    public void validarProductosRepetidos(InvoiceForm form) {

        Set<Integer> productosSeleccionados = new HashSet<>();

        for (var detalle : form.getDetails()) {

            if (detalle.isSelected()) {

                Integer productId = detalle.getProductId();

                if (productId != null &&
                        !productosSeleccionados.add(productId)) {

                    throw new IllegalArgumentException(
                            "No puede agregar el mismo producto más de una vez");
                }
            }
        }
    }

















}