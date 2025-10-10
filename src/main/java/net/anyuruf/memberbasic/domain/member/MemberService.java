package net.anyuruf.memberbasic.domain.member;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import net.anyuruf.memberbasic.config.DomainService;
import net.anyuruf.memberbasic.domain.member.api.MemberApi;
import net.anyuruf.memberbasic.domain.member.model.MemberBasic;
import net.anyuruf.memberbasic.domain.member.model.MemberBasicInput;
import net.anyuruf.memberbasic.domain.member.spi.MemberSpi;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@DomainService
@RequiredArgsConstructor
public class MemberService implements MemberApi {
	private final MemberSpi memberSpi;

	@Override
	public Mono<MemberBasic> addFamilyMember(MemberBasicInput memberBasicInput) {
		return memberSpi.addFamilyMember(memberBasicInput);
	}

	@Override
	public Flux<MemberBasic> getAllMembers() {
		return memberSpi.getAllMembers();
	}

	@Override
	public Mono<MemberBasic> getFamilyMember(UUID memberId) {
		return memberSpi.getFamilyMember(memberId);
	}

	@Override
	public Mono<MemberBasic> editFamilyMember(MemberBasic memberBasic) {
		return memberSpi.editFamilyMember(memberBasic);
	}

}
