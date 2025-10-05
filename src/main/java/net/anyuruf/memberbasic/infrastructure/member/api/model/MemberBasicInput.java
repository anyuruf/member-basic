package net.anyuruf.memberbasic.infrastructure.member.api.model;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import net.anyuruf.memberbasic.infrastructure.member.api.model.ApiGenderEnum.Gender;

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
