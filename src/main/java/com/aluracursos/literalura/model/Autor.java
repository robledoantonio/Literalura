package com.aluracursos.literalura.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nombre;
    private LocalDate nacimiento;
    private LocalDate deceso;

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalDate getNacimiento() { return nacimiento; }

    public void setNacimiento(LocalDate nacimiento) { this.nacimiento = nacimiento; }

    public LocalDate getDeceso() { return deceso; }

    public void setDeceso(LocalDate deceso) { this.deceso = deceso; }
}
