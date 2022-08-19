package net.anyuruf.memberbasic.entity;

import java.util.UUID;
import java.time.LocalDate;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.anyuruf.memberbasic.entity.GenderEnum.Gender;

@Data
@AllArgsConstructor
@Table
public class MemberBasic {

    @PrimaryKey
    private UUID id;
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate dob;

    MemberBasic() {
        this.id = UUID.randomUUID();
    }

    MemberBasic(String fn, String ln, Gender gn, LocalDate ld) {
        this();
        this.firstName = fn;
        this.lastName = ln;
        this.gender = gn;
        this.dob = ld;
    }
}
