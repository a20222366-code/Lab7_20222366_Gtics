package com.example.Lab7_20222366_Gtics.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class InvoiceForm {

    @NotBlank(message = "Debe seleccionar el tipo de comprobante")
    private String type;

    @NotNull(message = "La fecha es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate date;

    @NotNull(message = "Debe seleccionar un cliente")
    private Integer customerId;

    private List<InvoiceDetailForm> details = new ArrayList<>();

    public InvoiceForm() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public List<InvoiceDetailForm> getDetails() {
        return details;
    }

    public void setDetails(List<InvoiceDetailForm> details) {
        this.details = details;
    }
}
