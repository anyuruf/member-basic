package net.anyuruf.memberbasic.entity;

import java.time.LocalDate;
import javax.annotation.Nonnull;
import net.anyuruf.memberbasic.entity.GenderEnum.Gender;

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
