package com.exemplo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ManutencaoRepository extends JpaRepository<Manutencao, Long> {
    List<Manutencao> findByAeronave(Aeronave aeronave); // Ajuste para retornar uma lista
}
