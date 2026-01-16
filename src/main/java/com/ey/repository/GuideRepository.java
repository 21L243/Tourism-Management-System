package com.ey.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ey.entity.Guide;

public interface GuideRepository extends JpaRepository<Guide, Long> {
    
   
}
