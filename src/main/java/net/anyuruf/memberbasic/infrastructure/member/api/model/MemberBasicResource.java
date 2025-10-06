package net.anyuruf.memberbasic.infrastructure.member.api.model;

import java.time.LocalDate;
import java.util.UUID;

import net.anyuruf.memberbasic.domain.member.entity.GenderEnum;
import net.anyuruf.memberbasic.domain.member.entity.MemberBasic;
import net.anyuruf.memberbasic.domain.member.entity.MemberBasicInput;
import net.anyuruf.memberbasic.infrastructure.member.api.model.GenderEnumArch.GenderArch;
import net.anyuruf.memberbasic.infrastructure.member.spi.entity.GenderEnum.Gender;


public record MemberBasicResource(UUID uuid, String firstName, String lastName, String description, GenderArch genderArch,
		LocalDate dob) {
	/** Convert domain entity → resource */
    public MemberBasicResource(MemberBasic member) {
        this(
            member.id(),
            member.firstName(),
            member.lastName(),
            member.description(),
            GenderArch.valueOf(member.gender().name()),
            member.dob()
        );
    }

    /** Convert input → domain entity (for new member) */
    public static MemberBasicInput toDomain(MemberBasicRequest input) {
        return new MemberBasicInput(
            input.firstName(),
            input.lastName(),
            input.description(), 
            Gender.valueOf(input.genderArch().name()),
            input.dob()
        );
    }

    /** Convert edit input → domain entity (for updates) */
    public static MemberBasic toDomain(EditMemberRequest input) {
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
