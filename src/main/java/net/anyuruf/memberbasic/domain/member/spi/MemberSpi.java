package net.anyuruf.memberbasic.domain.member.spi;

import java.util.UUID;

import org.reactivestreams.Publisher;

import net.anyuruf.memberbasic.domain.member.entity.MemberBasic;
import net.anyuruf.memberbasic.domain.member.entity.MemberBasicInput;

public interface MemberSpi {
 	Publisher<MemberBasic> addFamilyMember(MemberBasicInput memberBasicInput);

    Publisher<MemberBasic> getAllMembers();

    Publisher<MemberBasic> getFamilyMember(UUID memberId);

    Publisher<MemberBasic> editFamilyMember(MemberBasic memberBasic);

}
