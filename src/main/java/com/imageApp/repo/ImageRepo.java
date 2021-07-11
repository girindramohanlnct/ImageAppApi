package com.imageApp.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.imageApp.model.Image;

@Repository	
public interface ImageRepo extends JpaRepository<Image, Integer> {
		public Optional<Image> findById(Integer id);
}
