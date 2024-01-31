package com.ssafy.backend.domain.place.repository;

import com.ssafy.backend.domain.place.entity.Place;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaceInfoRepository extends JpaRepository<Place, Long> {

    @Query(value = "select p from Place p where p.name = :name")
    Place findByName(String name);

}
