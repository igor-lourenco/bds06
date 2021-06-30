package com.devsuperior.movieflix.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;

@Service
public class ReviewService {
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private AuthService authService;

	@Transactional
	public ReviewDTO insert(ReviewDTO dto) {
		Review entity = new Review();
		copyDtoToEntity(dto, entity);
		System.out.println("Review "+entity.getText());
		entity = reviewRepository.save(entity);
		ReviewDTO reviewDTO = new ReviewDTO(entity);
		reviewDTO.setUser(authService.authenticated());
		return reviewDTO;
	}

	private void copyDtoToEntity(ReviewDTO dto, Review entity) {
		System.out.println("MovieId "+dto.getMovieId());
		System.out.println("MovieId "+dto.getUserId());
		entity.setMovie(movieRepository.getOne(dto.getMovieId()));
		entity.setUser(authService.authenticated());
		entity.setText(dto.getText());

	}	
}