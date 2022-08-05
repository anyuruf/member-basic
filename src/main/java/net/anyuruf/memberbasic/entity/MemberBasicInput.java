package net.anyuruf.memberbasic.entity;

import java.time.LocalDate;
import javax.annotation.Nonnull;
import net.anyuruf.memberbasic.entity.GenderEnum.Gender;

public class MemberBasicInput {
    @Nonnull
    public String firstName;
    @Nonnull
    public String lastName;
    @Nonnull
    public Gender gender;
    @Nonnull
    public LocalDate dob;
}
