package com.poly.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Reviews")
@Data
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ReviewID")
	private Integer reviewID;

	@ManyToOne
	@JoinColumn(name = "ProductID", nullable = false)
	private Product product;

	@ManyToOne
	@JoinColumn(name = "UserID", nullable = false)
	private User user;

	@Column(name = "Rating")
	private Integer rating;

	@Column(name = "Comment", columnDefinition = "NVARCHAR(MAX)")
	private String comment;

	@Column(name = "CreatedAt")
	private LocalDateTime createdAt;

	@Column(name = "Approved")
	private Boolean approved;
}
