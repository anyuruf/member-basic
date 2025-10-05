package net.anyuruf.memberbasic.domain.member.copy;

import java.util.UUID;

import org.reactivestreams.Publisher;

import lombok.RequiredArgsConstructor;
import net.anyuruf.memberbasic.domain.member.api.MemberApi;
import net.anyuruf.memberbasic.domain.member.entity.MemberBasic;
import net.anyuruf.memberbasic.domain.member.entity.MemberBasicInput;
import net.anyuruf.memberbasic.domain.member.spi.MemberSpi;


@Domain
@RequiredArgsConstructor
public class MemberService implements MemberApi {
	private final MemberSpi memberSpi;

	@Override
	public Publisher<MemberBasic> addFamilyMember(MemberBasicInput memberBasicInput) {
		return memberSpi.addFamilyMember(memberBasicInput);
	}

	@Override
	public Publisher<MemberBasic> getAllMembers() {
		return memberSpi.getAllMembers();
	}

	@Override
	public Publisher<MemberBasic> getFamilyMember(UUID memberId) {
		return memberSpi.getFamilyMember(memberId);
	}

	@Override
	public Publisher<MemberBasic> editFamilyMember(MemberBasic memberBasic) {
		return memberSpi.editFamilyMember(memberBasic);
	}

}
