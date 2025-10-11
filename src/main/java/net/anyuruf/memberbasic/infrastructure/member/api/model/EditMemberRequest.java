package net.anyuruf.memberbasic.infrastructure.member.api.model;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import net.anyuruf.memberbasic.infrastructure.member.api.model.GenderEnumArch.GenderArch;



public record EditMemberRequest(
    @NotNull(message = "ID is required for editing")
    UUID id,

    @NotBlank(message = "First name is required")
    String firstName,

    @NotBlank(message = "Last name is required")
    String lastName,

    
    String description,

    GenderArch genderArch,

   
    LocalDate dob
) {}
