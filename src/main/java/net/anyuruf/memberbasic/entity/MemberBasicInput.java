package net.anyuruf.memberbasic.entity;

import java.time.LocalDate;
import javax.annotation.Nonnull;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.anyuruf.memberbasic.entity.GenderEnum.Gender;

@AllArgsConstructor
@NoArgsConstructor
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
