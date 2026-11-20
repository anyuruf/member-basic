package net.anyuruf.memberbasic.infrastructure.member.api.model;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import net.anyuruf.memberbasic.infrastructure.member.api.model.GenderEnumArch.GenderArch;



public record MemberBasicRequest(
		 @NotBlank(message = "First name is required")
		 UUID fatherId,
		 
		 @NotNull(message = "ID is required for editing")
		 UUID motherId,
		 
		 @NotBlank(message = "First name is required")
		 String firstName,

		 @NotBlank(message = "Last name is required")
		 String lastName,

	
		 String description,

		 GenderArch genderArch,

		 LocalDate dob) {
}
