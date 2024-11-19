package com.exemplo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AeroportoRepository extends PagingAndSortingRepository<Aeroporto, Long>, JpaRepository<Aeroporto, Long> {
}
