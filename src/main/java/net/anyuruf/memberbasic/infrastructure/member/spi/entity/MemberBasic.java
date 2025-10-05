package net.anyuruf.memberbasic.infrastructure.member.spi.entity;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.annotation.Id;

import net.anyuruf.memberbasic.infrastructure.member.spi.entity.GenderEnum.Gender;

public record MemberBasic(@Id UUID id, String firstName, String lastName, String description, Gender gender,
		LocalDate dob) {
}
