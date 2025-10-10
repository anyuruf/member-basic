package net.anyuruf.memberbasic.infrastructure.member.spi.entity;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.annotation.Id;

import net.anyuruf.memberbasic.domain.member.model.GenderEnum.Gender;
import net.anyuruf.memberbasic.domain.member.model.MemberBasic;
import net.anyuruf.memberbasic.domain.member.model.MemberBasicInput;
import net.anyuruf.memberbasic.infrastructure.member.spi.entity.GenderEnumSpi.GenderSpi;

public record MemberBasicSpi(@Id UUID id, String firstName, String lastName, String description, GenderSpi gender,
		LocalDate dob) {
	//** Convert domain MemberBasicInut → Infrastructure MemberBasicSpi */
    public MemberBasicSpi(MemberBasicInput member) {
        this(
            null,
            member.firstName(),
            member.lastName(),
            member.description(),
            GenderSpi.valueOf(member.gender().name()),
            member.dob()
        );
    }
    
    //** Convert domain MemberBasic → Infrastructure MemberBasicSpi */
    public MemberBasicSpi(MemberBasic member) {
        this(
            member.id(),
            member.firstName(),
            member.lastName(),
            member.description(),
            GenderSpi.valueOf(member.gender().name()),
            member.dob()
        );
    }

    //** Convert SPI record → domain entity */
    public MemberBasic toDomain() {
        return new MemberBasic(
        		this.id(),
                this.firstName(),
                this.lastName(),
                this.description(),
                Gender.valueOf(this.gender().name()),
                this.dob()
        );
    }

}
