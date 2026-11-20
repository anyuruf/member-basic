package net.anyuruf.memberbasic.member.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.anyuruf.memberbasic.domain.member.api.MemberApi;
import net.anyuruf.memberbasic.domain.member.model.GenderEnum.Gender;
import net.anyuruf.memberbasic.domain.member.model.MemberBasic;
import net.anyuruf.memberbasic.domain.member.model.MemberBasicInput;
import net.anyuruf.memberbasic.domain.member.service.MemberService;
import net.anyuruf.memberbasic.domain.member.spi.MemberSpi;
import net.anyuruf.memberbasic.domain.member.spi.stub.MemberSpiStub;
import reactor.test.StepVerifier;


class MemberAPIFunctionalTest {

    private MemberApi memberApi;

    @BeforeEach
    void setUp() {
    	MemberSpi memberSpi = new MemberSpiStub();
        memberApi = new MemberService(memberSpi);
    }

    @Test
    void testAddMemberAndRetrieve() {
        MemberBasicInput input = new MemberBasicInput(
            null,
            null,
            "James",
            "Anyuru",
            "Test family member",
            Gender.MALE,
            LocalDate.of(2000, 5, 15)
        );

        StepVerifier.create(memberApi.addFamilyMember(input))
            .assertNext(created -> {
                assertNotNull(created.id());
                assertEquals("James", created.firstName());

                // Retrieve the newly created member
                StepVerifier.create(memberApi.getFamilyMember(created.id()))
                    .assertNext(retrieved -> {
                        assertEquals(created.id(), retrieved.id());
                        assertEquals(created.firstName(), retrieved.firstName());
                        assertEquals(created.lastName(), retrieved.lastName());
                    })
                    .verifyComplete();

                // Retrieve all members
                StepVerifier.create(memberApi.getAllMembers().collectList())
                    .assertNext(members -> {
                        assertEquals(6, members.size());
                        assertTrue(members.stream()
                            .anyMatch(member -> member.id().equals(created.id())));
                    })
                    .verifyComplete();
            })
            .verifyComplete();
    }

    @Test
    void testGetAllMembers() {
        StepVerifier.create(memberApi.getAllMembers().collectList())
            .assertNext(members -> assertEquals(5, members.size()))
            .verifyComplete();
    }

    @Test
    void testGetUnknownMember() {
        UUID unknownId = UUID.randomUUID();

        StepVerifier.create(memberApi.getFamilyMember(unknownId))
            .expectNextCount(0)
            .verifyComplete();
    }
    
    @Test
    void testEditMember() {
        UUID fatherId = UUID.fromString(
            "a1000000-0000-4000-8000-000000000001"
        );

        MemberBasic updatedMember = new MemberBasic(
            fatherId,
            null,
            null,
            "Jenesio",
            "Anyuru",
            "Updated description",
            Gender.MALE,
            LocalDate.of(1975, 4, 12)
        );

        StepVerifier.create(memberApi.editFamilyMember(updatedMember))
            .assertNext(result -> {
                assertEquals(fatherId, result.id());
                assertEquals("Updated description", result.description());
            })
            .verifyComplete();

        // Verify that the update was persisted in the stub
        StepVerifier.create(memberApi.getFamilyMember(fatherId))
            .assertNext(result ->
                assertEquals("Updated description", result.description())
            )
            .verifyComplete();
    }
}
