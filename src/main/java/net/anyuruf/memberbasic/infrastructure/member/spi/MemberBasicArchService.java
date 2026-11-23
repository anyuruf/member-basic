package net.anyuruf.memberbasic.infrastructure.member.spi;

import java.util.UUID;

import org.springframework.stereotype.Service;

import net.anyuruf.memberbasic.domain.member.model.MemberBasic;
import net.anyuruf.memberbasic.domain.member.model.MemberBasicInput;
import net.anyuruf.memberbasic.domain.member.spi.MemberSpi;
import net.anyuruf.memberbasic.infrastructure.member.spi.entity.MemberBasicSpi;
import net.anyuruf.memberbasic.infrastructure.member.spi.repository.CassandraMemberBasicRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class MemberBasicArchService implements MemberSpi  {
	private final CassandraMemberBasicRepository memberRepo;

	public MemberBasicArchService(CassandraMemberBasicRepository memberRepo) {
		this.memberRepo = memberRepo;
	}
	
	@Override
	public Mono<MemberBasic> addFamilyMember(MemberBasicInput member) {
		MemberBasicSpi memberSpi = new MemberBasicSpi(member);
		
		// Return transformed MemberBasic Mono from MemberBasicSpi Mono
		return memberRepo.save(memberSpi).map(MemberBasicSpi::toDomain);
	}

	@Override
	public Flux<MemberBasic> getAllMembers() {
		// Flux Stream of all MemberBasic from MemberBasicSpi 
		return memberRepo.findAll().map(MemberBasicSpi::toDomain);
	}

	@Override
	public Mono<MemberBasic> getFamilyMember(UUID memberId) {
		// Mono stream of MemberBasic  by their id/uuid
		return memberRepo.findById(memberId).map(MemberBasicSpi::toDomain);
	}

	@Override
	public Mono<MemberBasic> editFamilyMember(MemberBasic member) {
		MemberBasicSpi memberSpi = new MemberBasicSpi(member);
		// Mono stream of edited MemberBasic
		return memberRepo.save(memberSpi).map(MemberBasicSpi::toDomain);
	}

}
