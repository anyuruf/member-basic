package net.anyuruf.memberbasic.infrastructure.member.api.model;

import java.time.LocalDate;
import java.util.UUID;

import net.anyuruf.memberbasic.domain.member.entity.MemberBasic;
import net.anyuruf.memberbasic.domain.member.entity.GenderEnum.Gender;

public record MemberBasicResource(UUID uuid, String firstName, String lastName, String description, Gender gender,
		LocalDate dob) {
	/** Convert domain entity → resource */
    public MemberBasicResource(MemberBasic member) {
        this(
            member.id(),
            member.firstName(),
            member.lastName(),
            member.description(),
            member.gender(),
            member.dob()
        );
    }

    /** Convert input → domain entity (for new member) */
    public static MemberBasic toDomain(MemberBasicInput input) {
        return new MemberBasic(
            null, // id generated in DB
            input.firstName(),
            input.lastName(),
            input.description(), // description not part of schema
            input.gender(),
            input.dob()
        );
    }

    /** Convert edit input → domain entity (for updates) */
    public static MemberBasic toDomain(EditMemberInput input) {
        return new MemberBasic(
            input.id(), // ✅ now available in input
            input.firstName(),
            input.lastName(),
            input.description(),
            input.gender(),
            input.dob()
        );
    }
}
