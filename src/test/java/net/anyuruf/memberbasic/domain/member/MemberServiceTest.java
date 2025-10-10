package net.anyuruf.memberbasic.domain.member;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.anyuruf.memberbasic.domain.member.model.GenderEnum.Gender;
import net.anyuruf.memberbasic.domain.member.model.MemberBasic;
import net.anyuruf.memberbasic.domain.member.model.MemberBasicInput;
import net.anyuruf.memberbasic.domain.member.spi.MemberSpi;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
@DisplayName("MemberService Tests")
class MemberServiceTest {

    @Mock
    private MemberSpi memberSpi;

    @InjectMocks
    private MemberService memberService;

    private UUID testId;
    private MemberBasicInput testInput;
    private MemberBasic testMember;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testInput = new MemberBasicInput(
            "John",
            "Doe",
            "Test family member",
            Gender.MALE,
            LocalDate.of(1990, 1, 15)
        );
        testMember = new MemberBasic(
            testId,
            "John",
            "Doe",
            "Test family member",
            Gender.MALE,
            LocalDate.of(1990, 1, 15)
        );
    }

    @Nested
    @DisplayName("Add Family Member Tests")
    class AddFamilyMemberTests {

        @Test
        @DisplayName("Should successfully add a family member")
        void shouldSuccessfullyAddFamilyMember() {
            // Given
            when(memberSpi.addFamilyMember(testInput)).thenReturn(Mono.just(testMember));

            // When
            Mono<MemberBasic> result = memberService.addFamilyMember(testInput);

            // Then
            StepVerifier.create(result)
                .expectNext(testMember)
                .verifyComplete();

            verify(memberSpi, times(1)).addFamilyMember(testInput);
        }

        @Test
        @DisplayName("Should add female family member")
        void shouldAddFemaleFamilyMember() {
            // Given
            MemberBasicInput femaleInput = new MemberBasicInput(
                "Jane",
                "Smith",
                "Female family member",
                Gender.FEMALE,
                LocalDate.of(1985, 6, 20)
            );
            MemberBasic femaleMember = new MemberBasic(
                UUID.randomUUID(),
                "Jane",
                "Smith",
                "Female family member",
                Gender.FEMALE,
                LocalDate.of(1985, 6, 20)
            );
            when(memberSpi.addFamilyMember(femaleInput)).thenReturn(Mono.just(femaleMember));

            // When
            Mono<MemberBasic> result = memberService.addFamilyMember(femaleInput);

            // Then
            StepVerifier.create(result)
                .assertNext(member -> {
                    assertEquals(Gender.FEMALE, member.gender());
                    assertEquals("Jane", member.firstName());
                })
                .verifyComplete();
        }

        @Test
        @DisplayName("Should propagate error from SPI layer")
        void shouldPropagateErrorFromSpiLayer() {
            // Given
            RuntimeException expectedException = new RuntimeException("Database error");
            when(memberSpi.addFamilyMember(any())).thenReturn(Mono.error(expectedException));

            // When
            Mono<MemberBasic> result = memberService.addFamilyMember(testInput);

            // Then
            StepVerifier.create(result)
                .expectErrorMatches(throwable -> 
                    throwable instanceof RuntimeException &&
                    throwable.getMessage().equals("Database error"))
                .verify();
        }

        @Test
        @DisplayName("Should handle null input gracefully")
        void shouldHandleNullInputGracefully() {
            // Given
            when(memberSpi.addFamilyMember(null)).thenReturn(Mono.error(new IllegalArgumentException("Input cannot be null")));

            // When
            Mono<MemberBasic> result = memberService.addFamilyMember(null);

            // Then
            StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();
        }

        @Test
        @DisplayName("Should handle empty Mono from SPI")
        void shouldHandleEmptyMonoFromSpi() {
            // Given
            when(memberSpi.addFamilyMember(testInput)).thenReturn(Mono.empty());

            // When
            Mono<MemberBasic> result = memberService.addFamilyMember(testInput);

            // Then
            StepVerifier.create(result)
                .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Get All Members Tests")
    class GetAllMembersTests {

        @Test
        @DisplayName("Should retrieve all family members")
        void shouldRetrieveAllFamilyMembers() {
            // Given
            MemberBasic member1 = new MemberBasic(
                UUID.randomUUID(),
                "John",
                "Doe",
                "Member 1",
                Gender.MALE,
                LocalDate.of(1990, 1, 15)
            );
            MemberBasic member2 = new MemberBasic(
                UUID.randomUUID(),
                "Jane",
                "Smith",
                "Member 2",
                Gender.FEMALE,
                LocalDate.of(1985, 6, 20)
            );
            MemberBasic member3 = new MemberBasic(
                UUID.randomUUID(),
                "Bob",
                "Johnson",
                "Member 3",
                Gender.MALE,
                LocalDate.of(1975, 12, 10)
            );

            when(memberSpi.getAllMembers()).thenReturn(Flux.just(member1, member2, member3));

            // When
            Flux<MemberBasic> result = memberService.getAllMembers();

            // Then
            StepVerifier.create(result)
                .expectNext(member1)
                .expectNext(member2)
                .expectNext(member3)
                .verifyComplete();

            verify(memberSpi, times(1)).getAllMembers();
        }

        @Test
        @DisplayName("Should return empty Flux when no members exist")
        void shouldReturnEmptyFluxWhenNoMembersExist() {
            // Given
            when(memberSpi.getAllMembers()).thenReturn(Flux.empty());

            // When
            Flux<MemberBasic> result = memberService.getAllMembers();

            // Then
            StepVerifier.create(result)
                .verifyComplete();
        }

        @Test
        @DisplayName("Should propagate error from SPI layer")
        void shouldPropagateErrorFromSpiLayer() {
            // Given
            RuntimeException expectedException = new RuntimeException("Database connection error");
            when(memberSpi.getAllMembers()).thenReturn(Flux.error(expectedException));

            // When
            Flux<MemberBasic> result = memberService.getAllMembers();

            // Then
            StepVerifier.create(result)
                .expectErrorMatches(throwable -> 
                    throwable instanceof RuntimeException &&
                    throwable.getMessage().equals("Database connection error"))
                .verify();
        }

        @Test
        @DisplayName("Should handle large number of members")
        void shouldHandleLargeNumberOfMembers() {
            // Given
            Flux<MemberBasic> largeFlux = Flux.range(0, 1000)
                .map(i -> new MemberBasic(
                    UUID.randomUUID(),
                    "First" + i,
                    "Last" + i,
                    "Description " + i,
                    i % 2 == 0 ? Gender.MALE : Gender.FEMALE,
                    LocalDate.of(1990, 1, 1).plusDays(i)
                ));
            when(memberSpi.getAllMembers()).thenReturn(largeFlux);

            // When
            Flux<MemberBasic> result = memberService.getAllMembers();

            // Then
            StepVerifier.create(result)
                .expectNextCount(1000)
                .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Get Family Member Tests")
    class GetFamilyMemberTests {

        @Test
        @DisplayName("Should retrieve family member by ID")
        void shouldRetrieveFamilyMemberById() {
            // Given
            when(memberSpi.getFamilyMember(testId)).thenReturn(Mono.just(testMember));

            // When
            Mono<MemberBasic> result = memberService.getFamilyMember(testId);

            // Then
            StepVerifier.create(result)
                .expectNext(testMember)
                .verifyComplete();

            verify(memberSpi, times(1)).getFamilyMember(testId);
        }

        @Test
        @DisplayName("Should return empty Mono when member not found")
        void shouldReturnEmptyMonoWhenMemberNotFound() {
            // Given
            UUID nonExistentId = UUID.randomUUID();
            when(memberSpi.getFamilyMember(nonExistentId)).thenReturn(Mono.empty());

            // When
            Mono<MemberBasic> result = memberService.getFamilyMember(nonExistentId);

            // Then
            StepVerifier.create(result)
                .verifyComplete();
        }

        @Test
        @DisplayName("Should propagate error from SPI layer")
        void shouldPropagateErrorFromSpiLayer() {
            // Given
            RuntimeException expectedException = new RuntimeException("Database query error");
            when(memberSpi.getFamilyMember(testId)).thenReturn(Mono.error(expectedException));

            // When
            Mono<MemberBasic> result = memberService.getFamilyMember(testId);

            // Then
            StepVerifier.create(result)
                .expectErrorMatches(throwable -> 
                    throwable instanceof RuntimeException &&
                    throwable.getMessage().equals("Database query error"))
                .verify();
        }

        @Test
        @DisplayName("Should handle null UUID")
        void shouldHandleNullUuid() {
            // Given
            when(memberSpi.getFamilyMember(null)).thenReturn(Mono.error(new IllegalArgumentException("UUID cannot be null")));

            // When
            Mono<MemberBasic> result = memberService.getFamilyMember(null);

            // Then
            StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();
        }

        @Test
        @DisplayName("Should retrieve different members by different IDs")
        void shouldRetrieveDifferentMembersByDifferentIds() {
            // Given
            UUID id1 = UUID.randomUUID();
            UUID id2 = UUID.randomUUID();
            MemberBasic member1 = new MemberBasic(id1, "John", "Doe", "Member 1", Gender.MALE, LocalDate.of(1990, 1, 1));
            MemberBasic member2 = new MemberBasic(id2, "Jane", "Smith", "Member 2", Gender.FEMALE, LocalDate.of(1985, 1, 1));

            when(memberSpi.getFamilyMember(id1)).thenReturn(Mono.just(member1));
            when(memberSpi.getFamilyMember(id2)).thenReturn(Mono.just(member2));

            // When
            Mono<MemberBasic> result1 = memberService.getFamilyMember(id1);
            Mono<MemberBasic> result2 = memberService.getFamilyMember(id2);

            // Then
            StepVerifier.create(result1)
                .expectNext(member1)
                .verifyComplete();
            StepVerifier.create(result2)
                .expectNext(member2)
                .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Edit Family Member Tests")
    class EditFamilyMemberTests {

        @Test
        @DisplayName("Should successfully edit family member")
        void shouldSuccessfullyEditFamilyMember() {
            // Given
            MemberBasic updatedMember = new MemberBasic(
                testId,
                "John",
                "Smith",
                "Updated description",
                Gender.MALE,
                LocalDate.of(1990, 1, 15)
            );
            when(memberSpi.editFamilyMember(testMember)).thenReturn(Mono.just(updatedMember));

            // When
            Mono<MemberBasic> result = memberService.editFamilyMember(testMember);

            // Then
            StepVerifier.create(result)
                .expectNext(updatedMember)
                .verifyComplete();

            verify(memberSpi, times(1)).editFamilyMember(testMember);
        }

        @Test
        @DisplayName("Should edit member with different gender")
        void shouldEditMemberWithDifferentGender() {
            // Given
            MemberBasic genderChanged = new MemberBasic(
                testId,
                "Jordan",
                "Doe",
                "Gender changed",
                Gender.FEMALE,
                LocalDate.of(1990, 1, 15)
            );
            when(memberSpi.editFamilyMember(genderChanged)).thenReturn(Mono.just(genderChanged));

            // When
            Mono<MemberBasic> result = memberService.editFamilyMember(genderChanged);

            // Then
            StepVerifier.create(result)
                .assertNext(member -> assertEquals(Gender.FEMALE, member.gender()))
                .verifyComplete();
        }

        @Test
        @DisplayName("Should edit member with updated date of birth")
        void shouldEditMemberWithUpdatedDateOfBirth() {
            // Given
            LocalDate newDob = LocalDate.of(1991, 2, 20);
            MemberBasic dobChanged = new MemberBasic(
                testId,
                "John",
                "Doe",
                "DOB changed",
                Gender.MALE,
                newDob
            );
            when(memberSpi.editFamilyMember(dobChanged)).thenReturn(Mono.just(dobChanged));

            // When
            Mono<MemberBasic> result = memberService.editFamilyMember(dobChanged);

            // Then
            StepVerifier.create(result)
                .assertNext(member -> assertEquals(newDob, member.dob()))
                .verifyComplete();
        }

        @Test
        @DisplayName("Should propagate error from SPI layer")
        void shouldPropagateErrorFromSpiLayer() {
            // Given
            RuntimeException expectedException = new RuntimeException("Update failed");
            when(memberSpi.editFamilyMember(any())).thenReturn(Mono.error(expectedException));

            // When
            Mono<MemberBasic> result = memberService.editFamilyMember(testMember);

            // Then
            StepVerifier.create(result)
                .expectErrorMatches(throwable -> 
                    throwable instanceof RuntimeException &&
                    throwable.getMessage().equals("Update failed"))
                .verify();
        }

        @Test
        @DisplayName("Should handle null member input")
        void shouldHandleNullMemberInput() {
            // Given
            when(memberSpi.editFamilyMember(null)).thenReturn(Mono.error(new IllegalArgumentException("Member cannot be null")));

            // When
            Mono<MemberBasic> result = memberService.editFamilyMember(null);

            // Then
            StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();
        }

        @Test
        @DisplayName("Should return empty Mono if edit fails")
        void shouldReturnEmptyMonoIfEditFails() {
            // Given
            when(memberSpi.editFamilyMember(testMember)).thenReturn(Mono.empty());

            // When
            Mono<MemberBasic> result = memberService.editFamilyMember(testMember);

            // Then
            StepVerifier.create(result)
                .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should add and then retrieve member")
        void shouldAddAndThenRetrieveMember() {
            // Given
            when(memberSpi.addFamilyMember(testInput)).thenReturn(Mono.just(testMember));
            when(memberSpi.getFamilyMember(testId)).thenReturn(Mono.just(testMember));

            // When
            Mono<MemberBasic> addResult = memberService.addFamilyMember(testInput);
            Mono<MemberBasic> getResult = addResult.flatMap(member -> memberService.getFamilyMember(member.id()));

            // Then
            StepVerifier.create(getResult)
                .expectNext(testMember)
                .verifyComplete();
        }

        @Test
        @DisplayName("Should add, edit, and retrieve member")
        void shouldAddEditAndRetrieveMember() {
            // Given
            MemberBasic editedMember = new MemberBasic(
                testId,
                "John",
                "Smith",
                "Updated",
                Gender.MALE,
                LocalDate.of(1990, 1, 15)
            );

            when(memberSpi.addFamilyMember(testInput)).thenReturn(Mono.just(testMember));
            when(memberSpi.editFamilyMember(any())).thenReturn(Mono.just(editedMember));
            when(memberSpi.getFamilyMember(testId)).thenReturn(Mono.just(editedMember));

            // When
            Mono<MemberBasic> result = memberService.addFamilyMember(testInput)
                .flatMap(member -> {
                    MemberBasic toEdit = new MemberBasic(
                        member.id(),
                        member.firstName(),
                        "Smith",
                        "Updated",
                        member.gender(),
                        member.dob()
                    );
                    return memberService.editFamilyMember(toEdit);
                })
                .flatMap(member -> memberService.getFamilyMember(member.id()));

            // Then
            StepVerifier.create(result)
                .assertNext(member -> {
                    assertEquals("Smith", member.lastName());
                    assertEquals("Updated", member.description());
                })
                .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Reactive Behavior Tests")
    class ReactiveBehaviorTests {

        @Test
        @DisplayName("Should handle delayed response from SPI")
        void shouldHandleDelayedResponseFromSpi() {
            // Given
            when(memberSpi.addFamilyMember(testInput))
                .thenReturn(Mono.just(testMember).delayElement(java.time.Duration.ofMillis(100)));

            // When
            Mono<MemberBasic> result = memberService.addFamilyMember(testInput);

            // Then
            StepVerifier.create(result)
                .expectNext(testMember)
                .verifyComplete();
        }

        @Test
        @DisplayName("Should not block on getAllMembers")
        void shouldNotBlockOnGetAllMembers() {
            // Given
            Flux<MemberBasic> delayedFlux = Flux.range(0, 5)
                .delayElements(java.time.Duration.ofMillis(10))
                .map(i -> new MemberBasic(
                    UUID.randomUUID(),
                    "Name" + i,
                    "Last" + i,
                    "Desc",
                    Gender.MALE,
                    LocalDate.now()
                ));
            when(memberSpi.getAllMembers()).thenReturn(delayedFlux);

            // When
            Flux<MemberBasic> result = memberService.getAllMembers();

            // Then
            StepVerifier.create(result)
                .expectNextCount(5)
                .verifyComplete();
        }
    }
}