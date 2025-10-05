package net.anyuruf.memberbasic.domain.member.entity;

import java.time.LocalDate;
import java.util.UUID;

import net.anyuruf.memberbasic.domain.member.entity.GenderEnum.Gender;

public record MemberBasic(UUID id, String firstName, String lastName, String description, Gender gender, LocalDate dob) {
}
