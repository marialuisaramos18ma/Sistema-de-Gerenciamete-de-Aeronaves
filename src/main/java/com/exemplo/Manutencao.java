package com.exemplo;

import javax.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Manutencao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String nomeUsuario;
    private LocalDateTime dataAtualizacao;

    private boolean blindagemAeronave;
    private boolean cabineCentralPiloto;
    private boolean motoresTurbinas;
    private boolean assentosCintosSeguranca;
    private boolean tremPouso;

    @ManyToOne
    @JoinColumn(name = "aeronave_id", nullable = false)
    @JsonBackReference
    private Aeronave aeronave;

    public Manutencao() {}

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public boolean isBlindagemAeronave() {
        return blindagemAeronave;
    }

    public void setBlindagemAeronave(boolean blindagemAeronave) {
        this.blindagemAeronave = blindagemAeronave;
    }

    public boolean isCabineCentralPiloto() {
        return cabineCentralPiloto;
    }

    public void setCabineCentralPiloto(boolean cabineCentralPiloto) {
        this.cabineCentralPiloto = cabineCentralPiloto;
    }

    public boolean isMotoresTurbinas() {
        return motoresTurbinas;
    }

    public void setMotoresTurbinas(boolean motoresTurbinas) {
        this.motoresTurbinas = motoresTurbinas;
    }

    public boolean isAssentosCintosSeguranca() {
        return assentosCintosSeguranca;
    }

    public void setAssentosCintosSeguranca(boolean assentosCintosSeguranca) {
        this.assentosCintosSeguranca = assentosCintosSeguranca;
    }

    public boolean isTremPouso() {
        return tremPouso;
    }

    public void setTremPouso(boolean tremPouso) {
        this.tremPouso = tremPouso;
    }

    public Aeronave getAeronave() {
        return aeronave;
    }

    public void setAeronave(Aeronave aeronave) {
        this.aeronave = aeronave;
    }
}
