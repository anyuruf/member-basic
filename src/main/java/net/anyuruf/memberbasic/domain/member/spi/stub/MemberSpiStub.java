package net.anyuruf.memberbasic.domain.member.spi.stub;


import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.anyuruf.memberbasic.config.Stub;
import net.anyuruf.memberbasic.domain.member.model.MemberBasic;
import net.anyuruf.memberbasic.domain.member.model.MemberBasicInput;
import net.anyuruf.memberbasic.domain.member.spi.MemberSpi;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Stub
public class MemberSpiStub implements MemberSpi {

    private final Map<UUID, MemberBasic> members = new ConcurrentHashMap<>();
    private static final UUID FATHER_ID =
    	    UUID.fromString("a1000000-0000-4000-8000-000000000001");

    	private static final UUID MOTHER_ID =
    	    UUID.fromString("a1000000-0000-4000-8000-000000000002");
    	private static final String LAST_NAME = "Anyuru";

    	

    public MemberSpiStub() {
    	members.put(FATHER_ID, new MemberBasic(
                FATHER_ID, null, null, "Jenesio", LAST_NAME, null, null,
                LocalDate.of(1975, 4, 12)
            ));

            members.put(MOTHER_ID, new MemberBasic(
                MOTHER_ID, null, null, "Grace", "Omodo", null, null,
                LocalDate.of(1979, 8, 23)
            ));

            members.put(UUID.fromString("a1000000-0000-4000-8000-000000000003"),
                new MemberBasic(
                    UUID.fromString("a1000000-0000-4000-8000-000000000003"),
                    FATHER_ID, MOTHER_ID, "Daniel", LAST_NAME, null, null,
                    LocalDate.of(2003, 2, 14)
                ));

            members.put(UUID.fromString("a1000000-0000-4000-8000-000000000004"),
                new MemberBasic(
                    UUID.fromString("a1000000-0000-4000-8000-000000000004"),
                    FATHER_ID, MOTHER_ID, "Sarah", LAST_NAME, null, null,
                    LocalDate.of(2007, 6, 19)
                ));

            members.put(UUID.fromString("a1000000-0000-4000-8000-000000000005"),
                new MemberBasic(
                    UUID.fromString("a1000000-0000-4000-8000-000000000005"),
                    FATHER_ID, MOTHER_ID, "Michael", LAST_NAME, null, null,
                    LocalDate.of(2012, 11, 5)
                ));

    }
    
    

    @Override
    public Mono<MemberBasic> addFamilyMember(MemberBasicInput input) {
        return Mono.fromSupplier(() -> {
            UUID id = UUID.randomUUID();

            MemberBasic member = new MemberBasic(
            	id,
				input.fatherId(),
				input.motherId(),
				input.firstName(),
				input.lastName(),
				input.description(),
				input.gender(),
				input.dob()
            );

            members.put(id, member);
            return member;
        });
    }

    @Override
    public Flux<MemberBasic> getAllMembers() {
        return Flux.defer(() -> Flux.fromIterable(members.values()));
    }

    @Override
    public Mono<MemberBasic> getFamilyMember(UUID memberId) {
        return Mono.defer(() -> Mono.justOrEmpty(members.get(memberId)));
    }

    @Override
    public Mono<MemberBasic> editFamilyMember(MemberBasic memberBasic) {
        return Mono.defer(() -> {
            UUID id = memberBasic.id();

            if (id == null) {
                return Mono.empty();
            }

            return Mono.justOrEmpty(
                members.computeIfPresent(id, (key, existing) -> memberBasic)
            );
        });
    }
}
