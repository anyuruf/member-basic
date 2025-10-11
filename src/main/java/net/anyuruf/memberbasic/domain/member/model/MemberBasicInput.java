package net.anyuruf.memberbasic.domain.member.model;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import net.anyuruf.memberbasic.domain.member.model.GenderEnum.Gender;



public record MemberBasicInput(
	    @NotBlank(message = "First name is required")
	    String firstName,

	    @NotBlank(message = "Last name is required")
	    String lastName,

	    String description,

	    Gender gender,

	    LocalDate dob
	) {}