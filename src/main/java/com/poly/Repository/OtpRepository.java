package com.poly.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.Model.OtpToken;

public interface OtpRepository extends JpaRepository<OtpToken, Long> {

    Optional<OtpToken> findTopByEmailOrderByCreatedAtDesc(String email);
}
