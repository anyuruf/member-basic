package net.anyuruf.memberbasic.domain.member.spi;

import java.util.UUID;

import net.anyuruf.memberbasic.domain.member.model.MemberBasic;
import net.anyuruf.memberbasic.domain.member.model.MemberBasicInput;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MemberSpi {
 	Mono<MemberBasic> addFamilyMember(MemberBasicInput memberBasicInput);

    Flux<MemberBasic> getAllMembers();

    Mono<MemberBasic> getFamilyMember(UUID memberId);

    Mono<MemberBasic> editFamilyMember(MemberBasic memberBasic);

}
