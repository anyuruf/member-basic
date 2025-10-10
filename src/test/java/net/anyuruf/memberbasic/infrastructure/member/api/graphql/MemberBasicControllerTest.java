package net.anyuruf.memberbasic.infrastructure.member.api.graphql;

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

import net.anyuruf.memberbasic.domain.member.api.MemberApi;
import net.anyuruf.memberbasic.domain.member.model.GenderEnum.Gender;
import net.anyuruf.memberbasic.domain.member.model.MemberBasic;
import net.anyuruf.memberbasic.domain.member.model.MemberBasicInput;
import net.anyuruf.memberbasic.infrastructure.member.api.model.EditMemberRequest;
import net.anyuruf.memberbasic.infrastructure.member.api.model.GenderEnumArch.GenderArch;
import net.anyuruf.memberbasic.infrastructure.member.api.model.MemberBasicRequest;
import net.anyuruf.memberbasic.infrastructure.member.api.model.MemberBasicResource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
@DisplayName("MemberBasicController Tests")
class MemberBasicControllerTest {

    @Mock
    private MemberApi memberApi;

    @InjectMocks
    private MemberBasicController controller;

    private UUID testId;
    private MemberBasic testMember;
    private MemberBasicRequest testRequest;
    private EditMemberRequest testEditRequest;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testMember = new MemberBasic(
            testId,
            "John",
            "Doe",
            "Test description",
            Gender.MALE,
            LocalDate.of(1990, 1, 15)
        );
        testRequest = new MemberBasicRequest(
            "John",
            "Doe",
            "Test description",
            GenderArch.MALE,
            LocalDate.of(1990, 1, 15)
        );
        testEditRequest = new EditMemberRequest(
            testId,
            "John",
            "Smith",
            "Updated description",
            GenderArch.MALE,
            LocalDate.of(1990, 1, 15)
        );
    }

    @Nested
    @DisplayName("GetAllMembers Query Tests")
    class GetAllMembersTests {

        @Test
        @DisplayName("Should retrieve all members and convert to resources")
        void shouldRetrieveAllMembersAndConvertToResources() {
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
            when(memberApi.getAllMembers()).thenReturn(Flux.just(member1, member2));

            // When
            Flux<MemberBasicResource> result = controller.getAllMembers();

            // Then
            StepVerifier.create(result)
                .assertNext(resource -> {
                    assertEquals(member1.id(), resource.uuid());
                    assertEquals("John", resource.firstName());
                    assertEquals("Doe", resource.lastName());
                    assertEquals(GenderArch.MALE, resource.genderArch());
                })
                .assertNext(resource -> {
                    assertEquals(member2.id(), resource.uuid());
                    assertEquals("Jane", resource.firstName());
                    assertEquals("Smith", resource.lastName());
                    assertEquals(GenderArch.FEMALE, resource.genderArch());
                })
                .verifyComplete();

            verify(memberApi, times(1)).getAllMembers();
        }

        @Test
        @DisplayName("Should return empty Flux when no members exist")
        void shouldReturnEmptyFluxWhenNoMembers() {
            // Given
            when(memberApi.getAllMembers()).thenReturn(Flux.empty());

            // When
            Flux<MemberBasicResource> result = controller.getAllMembers();

            // Then
            StepVerifier.create(result)
                .verifyComplete();
        }

        @Test
        @DisplayName("Should propagate error from API layer")
        void shouldPropagateErrorFromApiLayer() {
            // Given
            RuntimeException expectedException = new RuntimeException("Database error");
            when(memberApi.getAllMembers()).thenReturn(Flux.error(expectedException));

            // When
            Flux<MemberBasicResource> result = controller.getAllMembers();

            // Then
            StepVerifier.create(result)
                .expectErrorMatches(throwable -> 
                    throwable instanceof RuntimeException &&
                    throwable.getMessage().equals("Database error"))
                .verify();
        }

        @Test
        @DisplayName("Should handle large result sets")
        void shouldHandleLargeResultSets() {
            // Given
            Flux<MemberBasic> largeFlux = Flux.range(0, 100)
                .map(i -> new MemberBasic(
                    UUID.randomUUID(),
                    "First" + i,
                    "Last" + i,
                    "Desc" + i,
                    i % 2 == 0 ? Gender.MALE : Gender.FEMALE,
                    LocalDate.of(1990, 1, 1).plusDays(i)
                ));
            when(memberApi.getAllMembers()).thenReturn(largeFlux);

            // When
            Flux<MemberBasicResource> result = controller.getAllMembers();

            // Then
            StepVerifier.create(result)
                .expectNextCount(100)
                .verifyComplete();
        }
    }

    @Nested
    @DisplayName("AddMember Mutation Tests")
    class AddMemberTests {

        @Test
        @DisplayName("Should add member and return resource")
        void shouldAddMemberAndReturnResource() {
            // Given
            when(memberApi.addFamilyMember(any(MemberBasicInput.class)))
                .thenReturn(Mono.just(testMember));

            // When
            Mono<MemberBasicResource> result = controller.addMember(testRequest);

            // Then
            StepVerifier.create(result)
                .assertNext(resource -> {
                    assertEquals(testId, resource.uuid());
                    assertEquals("John", resource.firstName());
                    assertEquals("Doe", resource.lastName());
                    assertEquals("Test description", resource.description());
                    assertEquals(GenderArch.MALE, resource.genderArch());
                    assertEquals(LocalDate.of(1990, 1, 15), resource.dob());
                })
                .verifyComplete();

            verify(memberApi, times(1)).addFamilyMember(any(MemberBasicInput.class));
        }

        @Test
        @DisplayName("Should add female member correctly")
        void shouldAddFemaleMemberCorrectly() {
            // Given
            MemberBasicRequest femaleRequest = new MemberBasicRequest(
                "Jane",
                "Smith",
                "Female member",
                GenderArch.FEMALE,
                LocalDate.of(1985, 6, 20)
            );
            MemberBasic femaleMember = new MemberBasic(
                UUID.randomUUID(),
                "Jane",
                "Smith",
                "Female member",
                Gender.FEMALE,
                LocalDate.of(1985, 6, 20)
            );
            when(memberApi.addFamilyMember(any(MemberBasicInput.class)))
                .thenReturn(Mono.just(femaleMember));

            // When
            Mono<MemberBasicResource> result = controller.addMember(femaleRequest);

            // Then
            StepVerifier.create(result)
                .assertNext(resource -> {
                    assertEquals("Jane", resource.firstName());
                    assertEquals(GenderArch.FEMALE, resource.genderArch());
                })
                .verifyComplete();
        }

        @Test
        @DisplayName("Should convert GenderArch to Gender correctly")
        void shouldConvertGenderArchToGenderCorrectly() {
            // Given
            when(memberApi.addFamilyMember(any(MemberBasicInput.class)))
                .thenAnswer(invocation -> {
                    MemberBasicInput input = invocation.getArgument(0);
                    assertEquals(Gender.MALE, input.gender());
                    return Mono.just(testMember);
                });

            // When
            Mono<MemberBasicResource> result = controller.addMember(testRequest);

            // Then
            StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();
        }

        @Test
        @DisplayName("Should propagate validation errors")
        void shouldPropagateValidationErrors() {
            // Given
            when(memberApi.addFamilyMember(any()))
                .thenReturn(Mono.error(new IllegalArgumentException("Validation failed")));

            // When
            Mono<MemberBasicResource> result = controller.addMember(testRequest);

            // Then
            StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();
        }

        @Test
        @DisplayName("Should handle API errors")
        void shouldHandleApiErrors() {
            // Given
            when(memberApi.addFamilyMember(any()))
                .thenReturn(Mono.error(new RuntimeException("Database error")));

            // When
            Mono<MemberBasicResource> result = controller.addMember(testRequest);

            // Then
            StepVerifier.create(result)
                .expectErrorMatches(throwable -> 
                    throwable instanceof RuntimeException &&
                    throwable.getMessage().equals("Database error"))
                .verify();
        }
    }

    @Nested
    @DisplayName("EditMember Mutation Tests")
    class EditMemberTests {

        @Test
        @DisplayName("Should edit member and return updated resource")
        void shouldEditMemberAndReturnUpdatedResource() {
            // Given
            MemberBasic updatedMember = new MemberBasic(
                testId,
                "John",
                "Smith",
                "Updated description",
                Gender.MALE,
                LocalDate.of(1990, 1, 15)
            );
            when(memberApi.editFamilyMember(any(MemberBasic.class)))
                .thenReturn(Mono.just(updatedMember));

            // When
            Mono<MemberBasicResource> result = controller.editMember(testEditRequest);

            // Then
            StepVerifier.create(result)
                .assertNext(resource -> {
                    assertEquals(testId, resource.uuid());
                    assertEquals("John", resource.firstName());
                    assertEquals("Smith", resource.lastName());
                    assertEquals("Updated description", resource.description());
                })
                .verifyComplete();

            verify(memberApi, times(1)).editFamilyMember(any(MemberBasic.class));
        }

        @Test
        @DisplayName("Should convert EditMemberRequest to MemberBasic correctly")
        void shouldConvertEditRequestToMemberBasicCorrectly() {
            // Given
            when(memberApi.editFamilyMember(any(MemberBasic.class)))
                .thenAnswer(invocation -> {
                    MemberBasic member = invocation.getArgument(0);
                    assertEquals(testId, member.id());
                    assertEquals("John", member.firstName());
                    assertEquals("Smith", member.lastName());
                    assertEquals(Gender.MALE, member.gender());
                    return Mono.just(member);
                });

            // When
            Mono<MemberBasicResource> result = controller.editMember(testEditRequest);

            // Then
            StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();
        }

        @Test
        @DisplayName("Should edit member with changed gender")
        void shouldEditMemberWithChangedGender() {
            // Given
            EditMemberRequest genderChangeRequest = new EditMemberRequest(
                testId,
                "Jordan",
                "Doe",
                "Gender changed",
                GenderArch.FEMALE,
                LocalDate.of(1990, 1, 15)
            );
            MemberBasic genderChangedMember = new MemberBasic(
                testId,
                "Jordan",
                "Doe",
                "Gender changed",
                Gender.FEMALE,
                LocalDate.of(1990, 1, 15)
            );
            when(memberApi.editFamilyMember(any(MemberBasic.class)))
                .thenReturn(Mono.just(genderChangedMember));

            // When
            Mono<MemberBasicResource> result = controller.editMember(genderChangeRequest);

            // Then
            StepVerifier.create(result)
                .assertNext(resource -> assertEquals(GenderArch.FEMALE, resource.genderArch()))
                .verifyComplete();
        }

        @Test
        @DisplayName("Should propagate errors from API layer")
        void shouldPropagateErrorsFromApiLayer() {
            // Given
            when(memberApi.editFamilyMember(any()))
                .thenReturn(Mono.error(new RuntimeException("Update failed")));

            // When
            Mono<MemberBasicResource> result = controller.editMember(testEditRequest);

            // Then
            StepVerifier.create(result)
                .expectErrorMatches(throwable -> 
                    throwable instanceof RuntimeException &&
                    throwable.getMessage().equals("Update failed"))
                .verify();
        }

        @Test
        @DisplayName("Should handle member not found")
        void shouldHandleMemberNotFound() {
            // Given
            when(memberApi.editFamilyMember(any()))
                .thenReturn(Mono.empty());

            // When
            Mono<MemberBasicResource> result = controller.editMember(testEditRequest);

            // Then
            StepVerifier.create(result)
                .verifyComplete();
        }
    }

    @Nested
    @DisplayName("GetFamilyMember Query Tests")
    class GetFamilyMemberTests {

        @Test
        @DisplayName("Should retrieve family member by UUID")
        void shouldRetrieveFamilyMemberByUuid() {
            // Given
            when(memberApi.getFamilyMember(testId)).thenReturn(Mono.just(testMember));

            // When
            Mono<MemberBasicResource> result = controller.getFamilyMember(testId);

            // Then
            StepVerifier.create(result)
                .assertNext(resource -> {
                    assertEquals(testId, resource.uuid());
                    assertEquals("John", resource.firstName());
                    assertEquals("Doe", resource.lastName());
                })
                .verifyComplete();

            verify(memberApi, times(1)).getFamilyMember(testId);
        }

        @Test
        @DisplayName("Should return empty Mono when member not found")
        void shouldReturnEmptyMonoWhenMemberNotFound() {
            // Given
            UUID nonExistentId = UUID.randomUUID();
            when(memberApi.getFamilyMember(nonExistentId)).thenReturn(Mono.empty());

            // When
            Mono<MemberBasicResource> result = controller.getFamilyMember(nonExistentId);

            // Then
            StepVerifier.create(result)
                .verifyComplete();
        }

        @Test
        @DisplayName("Should propagate errors from API layer")
        void shouldPropagateErrorsFromApiLayer() {
            // Given
            when(memberApi.getFamilyMember(testId))
                .thenReturn(Mono.error(new RuntimeException("Database error")));

            // When
            Mono<MemberBasicResource> result = controller.getFamilyMember(testId);

            // Then
            StepVerifier.create(result)
                .expectErrorMatches(throwable -> 
                    throwable instanceof RuntimeException &&
                    throwable.getMessage().equals("Database error"))
                .verify();
        }

        @Test
        @DisplayName("Should convert domain Gender to GenderArch correctly")
        void shouldConvertDomainGenderToGenderArchCorrectly() {
            // Given
            MemberBasic femaleMember = new MemberBasic(
                testId,
                "Jane",
                "Doe",
                "Female member",
                Gender.FEMALE,
                LocalDate.of(1990, 1, 15)
            );
            when(memberApi.getFamilyMember(testId)).thenReturn(Mono.just(femaleMember));

            // When
            Mono<MemberBasicResource> result = controller.getFamilyMember(testId);

            // Then
            StepVerifier.create(result)
                .assertNext(resource -> assertEquals(GenderArch.FEMALE, resource.genderArch()))
                .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should handle complete add-edit-get workflow")
        void shouldHandleCompleteAddEditGetWorkflow() {
            // Given
            MemberBasic addedMember = new MemberBasic(
                testId,
                "John",
                "Doe",
                "Initial",
                Gender.MALE,
                LocalDate.of(1990, 1, 15)
            );
            MemberBasic editedMember = new MemberBasic(
                testId,
                "John",
                "Smith",
                "Updated",
                Gender.MALE,
                LocalDate.of(1990, 1, 15)
            );

            when(memberApi.addFamilyMember(any())).thenReturn(Mono.just(addedMember));
            when(memberApi.editFamilyMember(any())).thenReturn(Mono.just(editedMember));
            when(memberApi.getFamilyMember(testId)).thenReturn(Mono.just(editedMember));

            // When & Then - Add
            StepVerifier.create(controller.addMember(testRequest))
                .assertNext(resource -> assertEquals("Doe", resource.lastName()))
                .verifyComplete();

            // When & Then - Edit
            StepVerifier.create(controller.editMember(testEditRequest))
                .assertNext(resource -> assertEquals("Smith", resource.lastName()))
                .verifyComplete();

            // When & Then - Get
            StepVerifier.create(controller.getFamilyMember(testId))
                .assertNext(resource -> {
                    assertEquals("Smith", resource.lastName());
                    assertEquals("Updated", resource.description());
                })
                .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Gender Conversion Tests")
    class GenderConversionTests {

        @Test
        @DisplayName("Should convert MALE from GenderArch to Gender")
        void shouldConvertMaleFromArchToGender() {
            // Given
            when(memberApi.addFamilyMember(any(MemberBasicInput.class)))
                .thenAnswer(invocation -> {
                    MemberBasicInput input = invocation.getArgument(0);
                    assertEquals(Gender.MALE, input.gender());
                    return Mono.just(testMember);
                });

            // When
            controller.addMember(testRequest).block();

            // Then
            verify(memberApi).addFamilyMember(any(MemberBasicInput.class));
        }

        @Test
        @DisplayName("Should convert FEMALE from GenderArch to Gender")
        void shouldConvertFemaleFromArchToGender() {
            // Given
            MemberBasicRequest femaleRequest = new MemberBasicRequest(
                "Jane", "Doe", "Desc", GenderArch.FEMALE, LocalDate.now()
            );
            MemberBasic femaleMember = new MemberBasic(
                UUID.randomUUID(), "Jane", "Doe", "Desc", Gender.FEMALE, LocalDate.now()
            );

            when(memberApi.addFamilyMember(any(MemberBasicInput.class)))
                .thenAnswer(invocation -> {
                    MemberBasicInput input = invocation.getArgument(0);
                    assertEquals(Gender.FEMALE, input.gender());
                    return Mono.just(femaleMember);
                });

            // When
            controller.addMember(femaleRequest).block();

            // Then
            verify(memberApi).addFamilyMember(any(MemberBasicInput.class));
        }

        @Test
        @DisplayName("Should convert MALE from Gender to GenderArch in response")
        void shouldConvertMaleFromGenderToArchInResponse() {
            // Given
            when(memberApi.getFamilyMember(testId)).thenReturn(Mono.just(testMember));

            // When
            MemberBasicResource resource = controller.getFamilyMember(testId).block();

            // Then
            assertNotNull(resource);
            assertEquals(GenderArch.MALE, resource.genderArch());
        }

        @Test
        @DisplayName("Should convert FEMALE from Gender to GenderArch in response")
        void shouldConvertFemaleFromGenderToArchInResponse() {
            // Given
            MemberBasic femaleMember = new MemberBasic(
                testId, "Jane", "Doe", "Desc", Gender.FEMALE, LocalDate.now()
            );
            when(memberApi.getFamilyMember(testId)).thenReturn(Mono.just(femaleMember));

            // When
            MemberBasicResource resource = controller.getFamilyMember(testId).block();

            // Then
            assertNotNull(resource);
            assertEquals(GenderArch.FEMALE, resource.genderArch());
        }
    }
}