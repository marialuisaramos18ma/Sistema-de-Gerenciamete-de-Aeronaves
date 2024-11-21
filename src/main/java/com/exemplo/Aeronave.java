package com.exemplo;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List; // Importando a classe List
import java.util.ArrayList; // Importando a classe ArrayList

@Entity
public class Aeronave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String modelo;
    private String tipoCombustivel;
    private String tipoMotor;
    private int quantidadeMotores;
    private int capacidadeTanque;
    private double peso;
    private double altitudeVoo;
    private double autonomia;

    @OneToMany(mappedBy = "aeronave", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Manutencao> manutencoes = new ArrayList<>(); // Inicializando a lista de manutenções

    public Aeronave() {}

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getTipoCombustivel() {
        return tipoCombustivel;
    }

    public void setTipoCombustivel(String tipoCombustivel) {
        this.tipoCombustivel = tipoCombustivel;
    }

    public String getTipoMotor() {
        return tipoMotor;
    }

    public void setTipoMotor(String tipoMotor) {
        this.tipoMotor = tipoMotor;
    }

    public int getQuantidadeMotores() {
        return quantidadeMotores;
    }

    public void setQuantidadeMotores(int quantidadeMotores) {
        this.quantidadeMotores = quantidadeMotores;
    }

    public int getCapacidadeTanque() {
        return capacidadeTanque;
    }

    public void setCapacidadeTanque(int capacidadeTanque) {
        this.capacidadeTanque = capacidadeTanque;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getAltitudeVoo() {
        return altitudeVoo;
    }

    public void setAltitudeVoo(double altitudeVoo) {
        this.altitudeVoo = altitudeVoo;
    }

    public double getAutonomia() {
        return autonomia;
    }

    public void setAutonomia(double autonomia) {
        this.autonomia = autonomia;
    }

    public List<Manutencao> getManutencoes() {
        return manutencoes;
    }

    public void setManutencoes(List<Manutencao> manutencoes) {
        this.manutencoes = manutencoes;
    }

    // Métodos para adicionar e remover manutenções
    public void addManutencao(Manutencao manutencao) {
        manutencoes.add(manutencao);
        manutencao.setAeronave(this);
    }

    public void removeManutencao(Manutencao manutencao) {
        manutencoes.remove(manutencao);
        manutencao.setAeronave(null);
    }
}
