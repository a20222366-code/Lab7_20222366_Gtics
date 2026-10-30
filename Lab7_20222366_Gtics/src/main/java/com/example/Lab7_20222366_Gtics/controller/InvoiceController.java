package com.example.Lab7_20222366_Gtics.controller;

import com.example.Lab7_20222366_Gtics.repository.CustomerRepository;
import com.example.Lab7_20222366_Gtics.repository.InvoiceRepository;
import com.example.Lab7_20222366_Gtics.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/comprobantes")
public class InvoiceController {

    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final InvoiceService invoiceService;

    public InvoiceController(
            InvoiceRepository invoiceRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository,
            InvoiceService invoiceService) {
        this.invoiceRepository = invoiceRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public String listar(Model model) {
        List<Invoice> comprobantes = invoiceRepository.findAll();
        model.addAttribute("comprobantes", comprobantes);
        return "comprobantes/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("invoiceForm", new InvoiceForm());
        cargarDatosFormulario(model);
        return "comprobantes/formulario";
    }

    private void cargarDatosFormulario(Model model) {
        List<Customer> clientes = customerRepository.findAll();
        List<Product> productos = productRepository.findAll();

        model.addAttribute("clientes", clientes);
        model.addAttribute("productos", productos);
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute("invoiceForm") InvoiceForm invoiceForm,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            cargarDatosFormulario(model);
            return "comprobantes/formulario";
        }

        try {
            invoiceService.guardarComprobante(invoiceForm);
            redirectAttributes.addFlashAttribute(
                    "mensaje", "Comprobante registrado correctamente");
            return "redirect:/comprobantes";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorGlobal", e.getMessage());
            cargarDatosFormulario(model);
            return "comprobantes/formulario";
        }
    }
}