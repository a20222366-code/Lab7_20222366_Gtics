package com.example.Lab7_20222366_Gtics.controller;



import com.example.Lab7_20222366_Gtics.entity.Customer;
import com.example.Lab7_20222366_Gtics.repository.CustomerRepository;
import com.example.Lab7_20222366_Gtics.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clientes")
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final CustomerService customerService;

    public CustomerController(
            CustomerRepository customerRepository,
            CustomerService customerService) {
        this.customerRepository = customerRepository;
        this.customerService = customerService;
    }

    @GetMapping
    public String listar(Model model) {

        model.addAttribute("clientes", customerRepository.findAll());
        return "clientes/lista";
    }


    @GetMapping("/nuevo")
    public String nuevo(Model model) {

        model.addAttribute("customer", new Customer());
        return "clientes/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(

            @Valid @ModelAttribute("customer") Customer customer,
            BindingResult result,
            Model model) {


        if (!customerService.documentoValidoParaTipo(customer.getDocument(), customer.getDocumentType())) {

            result.rejectValue(
                    "document",
                    "documento.invalido",
                    "El DNI debe tener 8 dígitos y el RUC 11 dígitos numéricos");
        }


        if (customerService.documentoDuplicado(
                customer.getDocument(), customer.getId())) {
            result.rejectValue(
                    "document",
                    "documento.duplicado",
                    "Este documento ya está registrado");
        }

        if (result.hasErrors()) {

            return "clientes/formulario";
        }


        customerService.guardar(customer);
        return "redirect:/clientes";
    }



    @GetMapping("/editar/{id}")
    public String editar(

            @PathVariable Integer id,
            Model model) {


        Customer customer = customerRepository.findById(id).orElse(null);


        if (customer == null) {
            return "redirect:/clientes";
        }


        model.addAttribute("customer", customer);
        return "clientes/formulario";
    }


    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {


        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
        }


        return "redirect:/clientes";
    }
}
