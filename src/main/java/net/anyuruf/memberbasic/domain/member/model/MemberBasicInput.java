package net.anyuruf.memberbasic.domain.member.model;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import net.anyuruf.memberbasic.domain.member.model.GenderEnum.Gender;



public record MemberBasicInput(
	    @NotBlank(message = "First name is required")
	    String firstName,

	    @NotBlank(message = "Last name is required")
	    String lastName,

	    @NotBlank(message = "Description is required")
	    String description,

	    Gender gender,

	    @NotNull(message = "Date of birth is required")
	    LocalDate dob
	) {}