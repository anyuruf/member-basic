package net.anyuruf.memberbasic.domain.member.entity;

import java.time.LocalDate;

import javax.annotation.Nonnull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.anyuruf.memberbasic.entity.GenderEnum.Gender;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberBasicInput {
    @Nonnull
    private String firstName;
    @Nonnull
    private String lastName;
    @Nonnull
    private Gender gender;
    @Nonnull
    private LocalDate dob;
}
