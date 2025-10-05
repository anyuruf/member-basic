package net.anyuruf.memberbasic.infrastructure.member.api.model;

import java.time.LocalDate;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import net.anyuruf.memberbasic.infrastructure.member.api.model.ApiGenderEnum.Gender;

public record EditMemberInput(
    @NotNull(message = "ID is required for editing")
    UUID id,

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
