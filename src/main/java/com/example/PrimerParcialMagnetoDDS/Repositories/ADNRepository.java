package com.example.PrimerParcialMagnetoDDS.Repositories;

import com.example.PrimerParcialMagnetoDDS.Entidades.ADN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ADNRepository extends JpaRepository<ADN, Long> {

    Optional<ADN> findByDna(String dna);

    @Query("SELECT COUNT(a) FROM ADN a WHERE a.EsMutante=true ")
    long countMutant();


    @Query("SELECT COUNT(a) FROM ADN a WHERE a.EsMutante=false ")
    long countHuman();


}
