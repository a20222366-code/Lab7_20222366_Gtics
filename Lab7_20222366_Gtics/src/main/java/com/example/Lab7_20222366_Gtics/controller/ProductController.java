package com.example.Lab7_20222366_Gtics.controller;


import com.example.Lab7_20222366_Gtics.entity.Product;
import com.example.Lab7_20222366_Gtics.repository.ProductRepository;
import com.example.Lab7_20222366_Gtics.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/productos")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductService productService;

    public ProductController(ProductRepository productRepository,
                             ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }


    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", productRepository.findAll());
        return "productos/lista";
    }


    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("product", new Product());
        return "productos/formulario";
    }


    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute("product") Product product,
            BindingResult result) {


        if (product.getName() != null && !product.getName().isBlank()
                && productService.nombreDuplicado(
                product.getName().trim(), product.getId())) {

            result.rejectValue(
                    "name",
                    "nombre.duplicado",
                    "Ya existe un producto con ese nombre");
        }


        if (result.hasErrors()) {
            return "productos/formulario";
        }

        product.setName(product.getName().trim());
        productService.guardar(product);

        return "redirect:/productos";
    }


    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Product product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return "redirect:/productos";
        }

        model.addAttribute("product", product);
        return "productos/formulario";
    }


    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        }

        return "redirect:/productos";
    }
}