package net.anyuruf.memberbasic.infrastructure.member.api.model;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import net.anyuruf.memberbasic.infrastructure.member.api.model.GenderEnumArch.GenderArch;



public record MemberBasicRequest(
		 @NotBlank(message = "First name is required")
		    String firstName,

		    @NotBlank(message = "Last name is required")
		    String lastName,

		    @NotBlank(message = "Description is required")
		    String description,

		    GenderArch genderArch,

		    @NotNull(message = "Date of birth is required")
		    LocalDate dob) {
}
