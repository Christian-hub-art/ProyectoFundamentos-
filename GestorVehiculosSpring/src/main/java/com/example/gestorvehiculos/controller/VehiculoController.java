package com.example.gestorvehiculos.controller;

import com.example.gestorvehiculos.model.Vehiculo;
import com.example.gestorvehiculos.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vehiculos")
public class VehiculoController {

    @Autowired
    private VehiculoRepository repository;

    // Mostrar lista de vehículos y formulario
    @GetMapping({"", "/nuevo"})
    public String listarYForm(Model model) {
        model.addAttribute("vehiculos", repository.findAll());
        model.addAttribute("vehiculo", new Vehiculo()); // <- necesario para el formulario
        return "index";
    }

    // Guardar nuevo vehículo
    @PostMapping
    public String guardar(@ModelAttribute Vehiculo vehiculo) {
        repository.save(vehiculo);
        return "redirect:/vehiculos";
    }

    // Eliminar vehículo por id
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/vehiculos";
    }
}
