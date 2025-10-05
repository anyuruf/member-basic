package net.anyuruf.memberbasic.infrastructure.member.api.model;

import java.time.LocalDate;
import java.util.UUID;

import net.anyuruf.memberbasic.infrastructure.member.spi.entity.GenderEnum.Gender;

public record MemberBasicRequest(UUID uuid, String firstName, String lastName, String description, Gender gender,
		LocalDate dob) {
}
