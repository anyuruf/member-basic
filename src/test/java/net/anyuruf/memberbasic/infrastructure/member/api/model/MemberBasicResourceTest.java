package net.anyuruf.memberbasic.infrastructure.member.api.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import net.anyuruf.memberbasic.domain.member.model.GenderEnum.Gender;
import net.anyuruf.memberbasic.domain.member.model.MemberBasic;
import net.anyuruf.memberbasic.domain.member.model.MemberBasicInput;
import net.anyuruf.memberbasic.infrastructure.member.api.model.GenderEnumArch.GenderArch;

@DisplayName("MemberBasicResource Tests")
class MemberBasicResourceTest {

    @Nested
    @DisplayName("Constructor from MemberBasic Tests")
    class ConstructorFromMemberBasicTests {

        @Test
        @DisplayName("Should create resource from domain MemberBasic with MALE gender")
        void shouldCreateResourceFromDomainMemberBasicMale() {
            // Given
            UUID id = UUID.randomUUID();
            LocalDate dob = LocalDate.of(1990, 1, 15);
            MemberBasic member = new MemberBasic(
                id,
                "John",
                "Doe",
                "Test description",
                Gender.MALE,
                dob
            );

            // When
            MemberBasicResource resource = new MemberBasicResource(member);

            // Then
            assertEquals(id, resource.uuid());
            assertEquals("John", resource.firstName());
            assertEquals("Doe", resource.lastName());
            assertEquals("Test description", resource.description());
            assertEquals(GenderArch.MALE, resource.genderArch());
            assertEquals(dob, resource.dob());
        }

        @Test
        @DisplayName("Should create resource from domain MemberBasic with FEMALE gender")
        void shouldCreateResourceFromDomainMemberBasicFemale() {
            // Given
            MemberBasic member = new MemberBasic(
                UUID.randomUUID(),
                "Jane",
                "Smith",
                "Female member",
                Gender.FEMALE,
                LocalDate.of(1985, 6, 20)
            );

            // When
            MemberBasicResource resource = new MemberBasicResource(member);

            // Then
            assertEquals("Jane", resource.firstName());
            assertEquals(GenderArch.FEMALE, resource.genderArch());
        }

        @Test
        @DisplayName("Should convert gender enum correctly")
        void shouldConvertGenderEnumCorrectly() {
            // Given - MALE
            MemberBasic maleMember = new MemberBasic(
                UUID.randomUUID(),
                "John",
                "Doe",
                "Male",
                Gender.MALE,
                LocalDate.now()
            );

            // When
            MemberBasicResource maleResource = new MemberBasicResource(maleMember);

            // Then
            assertEquals(GenderArch.MALE, maleResource.genderArch());

            // Given - FEMALE
            MemberBasic femaleMember = new MemberBasic(
                UUID.randomUUID(),
                "Jane",
                "Doe",
                "Female",
                Gender.FEMALE,
                LocalDate.now()
            );

            // When
            MemberBasicResource femaleResource = new MemberBasicResource(femaleMember);

            // Then
            assertEquals(GenderArch.FEMALE, femaleResource.genderArch());
        }

        @Test
        @DisplayName("Should handle null id in domain member")
        void shouldHandleNullIdInDomainMember() {
            // Given
            MemberBasic member = new MemberBasic(
                null,
                "John",
                "Doe",
                "No ID",
                Gender.MALE,
                LocalDate.now()
            );

            // When
            MemberBasicResource resource = new MemberBasicResource(member);

            // Then
            assertNull(resource.uuid());
        }

        @Test
        @DisplayName("Should preserve all field values during conversion")
        void shouldPreserveAllFieldValuesDuringConversion() {
            // Given
            UUID id = UUID.randomUUID();
            String firstName = "Jean-Pierre";
            String lastName = "O'Brien-Smith";
            String description = "Complex name with special chars: @#$";
            LocalDate dob = LocalDate.of(2000, 2, 29); // Leap day
            MemberBasic member = new MemberBasic(id, firstName, lastName, description, Gender.MALE, dob);

            // When
            MemberBasicResource resource = new MemberBasicResource(member);

            // Then
            assertEquals(id, resource.uuid());
            assertEquals(firstName, resource.firstName());
            assertEquals(lastName, resource.lastName());
            assertEquals(description, resource.description());
            assertEquals(dob, resource.dob());
        }
    }

    @Nested
    @DisplayName("Static toDomain from MemberBasicRequest Tests")
    class ToDomainFromRequestTests {

        @Test
        @DisplayName("Should convert MemberBasicRequest to domain MemberBasicInput")
        void shouldConvertRequestToDomainInput() {
            // Given
            MemberBasicRequest request = new MemberBasicRequest(
                "John",
                "Doe",
                "Request description",
                GenderArch.MALE,
                LocalDate.of(1990, 5, 15)
            );

            // When
            MemberBasicInput input = MemberBasicResource.toDomain(request);

            // Then
            assertNotNull(input);
            assertEquals("John", input.firstName());
            assertEquals("Doe", input.lastName());
            assertEquals("Request description", input.description());
            assertEquals(Gender.MALE, input.gender());
            assertEquals(LocalDate.of(1990, 5, 15), input.dob());
        }

        @Test
        @DisplayName("Should convert FEMALE gender from request to domain")
        void shouldConvertFemaleGenderFromRequestToDomain() {
            // Given
            MemberBasicRequest request = new MemberBasicRequest(
                "Jane",
                "Smith",
                "Female request",
                GenderArch.FEMALE,
                LocalDate.of(1985, 6, 20)
            );

            // When
            MemberBasicInput input = MemberBasicResource.toDomain(request);

            // Then
            assertEquals(Gender.FEMALE, input.gender());
        }

        @Test
        @DisplayName("Should preserve special characters in conversion")
        void shouldPreserveSpecialCharactersInConversion() {
            // Given
            MemberBasicRequest request = new MemberBasicRequest(
                "José",
                "Müller",
                "Unicode: 日本語",
                GenderArch.MALE,
                LocalDate.of(1992, 3, 15)
            );

            // When
            MemberBasicInput input = MemberBasicResource.toDomain(request);

            // Then
            assertEquals("José", input.firstName());
            assertEquals("Müller", input.lastName());
            assertEquals("Unicode: 日本語", input.description());
        }
    }

    @Nested
    @DisplayName("Static toDomain from EditMemberRequest Tests")
    class ToDomainFromEditRequestTests {

        @Test
        @DisplayName("Should convert EditMemberRequest to domain MemberBasic")
        void shouldConvertEditRequestToDomainMemberBasic() {
            // Given
            UUID id = UUID.randomUUID();
            EditMemberRequest editRequest = new EditMemberRequest(
                id,
                "John",
                "Smith",
                "Updated description",
                GenderArch.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            MemberBasic member = MemberBasicResource.toDomain(editRequest);

            // Then
            assertNotNull(member);
            assertEquals(id, member.id());
            assertEquals("John", member.firstName());
            assertEquals("Smith", member.lastName());
            assertEquals("Updated description", member.description());
            assertEquals(Gender.MALE, member.gender());
            assertEquals(LocalDate.of(1990, 1, 15), member.dob());
        }

        @Test
        @DisplayName("Should convert FEMALE gender from edit request to domain")
        void shouldConvertFemaleGenderFromEditRequestToDomain() {
            // Given
            EditMemberRequest editRequest = new EditMemberRequest(
                UUID.randomUUID(),
                "Jane",
                "Doe",
                "Female edit",
                GenderArch.FEMALE,
                LocalDate.of(1985, 6, 20)
            );

            // When
            MemberBasic member = MemberBasicResource.toDomain(editRequest);

            // Then
            assertEquals(Gender.FEMALE, member.gender());
        }

        @Test
        @DisplayName("Should preserve ID during edit request conversion")
        void shouldPreserveIdDuringEditRequestConversion() {
            // Given
            UUID specificId = UUID.randomUUID();
            EditMemberRequest editRequest = new EditMemberRequest(
                specificId,
                "Test",
                "User",
                "Description",
                GenderArch.MALE,
                LocalDate.now()
            );

            // When
            MemberBasic member = MemberBasicResource.toDomain(editRequest);

            // Then
            assertEquals(specificId, member.id());
        }
    }

    @Nested
    @DisplayName("Record Properties Tests")
    class RecordPropertiesTests {

        @Test
        @DisplayName("Should create resource with direct constructor")
        void shouldCreateResourceWithDirectConstructor() {
            // Given
            UUID id = UUID.randomUUID();
            LocalDate dob = LocalDate.of(1990, 1, 15);

            // When
            MemberBasicResource resource = new MemberBasicResource(
                id,
                "John",
                "Doe",
                "Direct construction",
                GenderArch.MALE,
                dob
            );

            // Then
            assertEquals(id, resource.uuid());
            assertEquals("John", resource.firstName());
            assertEquals("Doe", resource.lastName());
            assertEquals("Direct construction", resource.description());
            assertEquals(GenderArch.MALE, resource.genderArch());
            assertEquals(dob, resource.dob());
        }

        @Test
        @DisplayName("Should be equal when all fields match")
        void shouldBeEqualWhenAllFieldsMatch() {
            // Given
            UUID id = UUID.randomUUID();
            LocalDate dob = LocalDate.of(1990, 1, 15);
            MemberBasicResource resource1 = new MemberBasicResource(
                id, "John", "Doe", "Desc", GenderArch.MALE, dob
            );
            MemberBasicResource resource2 = new MemberBasicResource(
                id, "John", "Doe", "Desc", GenderArch.MALE, dob
            );

            // Then
            assertEquals(resource1, resource2);
            assertEquals(resource1.hashCode(), resource2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when fields differ")
        void shouldNotBeEqualWhenFieldsDiffer() {
            // Given
            LocalDate dob = LocalDate.of(1990, 1, 15);
            MemberBasicResource resource1 = new MemberBasicResource(
                UUID.randomUUID(), "John", "Doe", "Desc", GenderArch.MALE, dob
            );
            MemberBasicResource resource2 = new MemberBasicResource(
                UUID.randomUUID(), "Jane", "Doe", "Desc", GenderArch.FEMALE, dob
            );

            // Then
            assertNotEquals(resource1, resource2);
        }

        @Test
        @DisplayName("Should have meaningful toString")
        void shouldHaveMeaningfulToString() {
            // Given
            UUID id = UUID.randomUUID();
            MemberBasicResource resource = new MemberBasicResource(
                id,
                "John",
                "Doe",
                "Description",
                GenderArch.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            String toString = resource.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.contains("John"));
            assertTrue(toString.contains("Doe"));
            assertTrue(toString.contains(id.toString()));
        }
    }

    @Nested
    @DisplayName("Round-Trip Conversion Tests")
    class RoundTripConversionTests {

        @Test
        @DisplayName("Should maintain data integrity through domain -> resource conversion")
        void shouldMaintainDataIntegrityThroughDomainToResource() {
            // Given
            UUID id = UUID.randomUUID();
            MemberBasic originalMember = new MemberBasic(
                id,
                "John",
                "Doe",
                "Original",
                Gender.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            MemberBasicResource resource = new MemberBasicResource(originalMember);

            // Then
            assertEquals(originalMember.id(), resource.uuid());
            assertEquals(originalMember.firstName(), resource.firstName());
            assertEquals(originalMember.lastName(), resource.lastName());
            assertEquals(originalMember.description(), resource.description());
            assertEquals(originalMember.dob(), resource.dob());
            assertEquals(originalMember.gender().name(), resource.genderArch().name());
        }

        @Test
        @DisplayName("Should maintain data integrity through request -> domain conversion")
        void shouldMaintainDataIntegrityThroughRequestToDomain() {
            // Given
            MemberBasicRequest request = new MemberBasicRequest(
                "John",
                "Doe",
                "Request",
                GenderArch.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            MemberBasicInput input = MemberBasicResource.toDomain(request);

            // Then
            assertEquals(request.firstName(), input.firstName());
            assertEquals(request.lastName(), input.lastName());
            assertEquals(request.description(), input.description());
            assertEquals(request.dob(), input.dob());
            assertEquals(request.genderArch().name(), input.gender().name());
        }

        @Test
        @DisplayName("Should maintain data integrity through edit request -> domain conversion")
        void shouldMaintainDataIntegrityThroughEditRequestToDomain() {
            // Given
            UUID id = UUID.randomUUID();
            EditMemberRequest editRequest = new EditMemberRequest(
                id,
                "John",
                "Smith",
                "Edit",
                GenderArch.FEMALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            MemberBasic member = MemberBasicResource.toDomain(editRequest);

            // Then
            assertEquals(editRequest.id(), member.id());
            assertEquals(editRequest.firstName(), member.firstName());
            assertEquals(editRequest.lastName(), member.lastName());
            assertEquals(editRequest.description(), member.description());
            assertEquals(editRequest.dob(), member.dob());
            assertEquals(editRequest.genderArch().name(), member.gender().name());
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null values in direct constructor")
        void shouldHandleNullValuesInDirectConstructor() {
            // When
            MemberBasicResource resource = new MemberBasicResource(
                null, null, null, null, null, null
            );

            // Then
            assertNull(resource.uuid());
            assertNull(resource.firstName());
            assertNull(resource.lastName());
            assertNull(resource.description());
            assertNull(resource.genderArch());
            assertNull(resource.dob());
        }

        @Test
        @DisplayName("Should handle empty strings")
        void shouldHandleEmptyStrings() {
            // When
            MemberBasicResource resource = new MemberBasicResource(
                UUID.randomUUID(), "", "", "", GenderArch.MALE, LocalDate.now()
            );

            // Then
            assertEquals("", resource.firstName());
            assertEquals("", resource.lastName());
            assertEquals("", resource.description());
        }

        @Test
        @DisplayName("Should handle very long strings")
        void shouldHandleVeryLongStrings() {
            // Given
            String longString = "A".repeat(1000);

            // When
            MemberBasicResource resource = new MemberBasicResource(
                UUID.randomUUID(),
                longString,
                longString,
                longString,
                GenderArch.MALE,
                LocalDate.now()
            );

            // Then
            assertEquals(longString, resource.firstName());
            assertEquals(longString, resource.lastName());
            assertEquals(longString, resource.description());
        }

        @Test
        @DisplayName("Should handle leap year dates")
        void shouldHandleLeapYearDates() {
            // Given
            LocalDate leapDay = LocalDate.of(2000, 2, 29);

            // When
            MemberBasicResource resource = new MemberBasicResource(
                UUID.randomUUID(),
                "Leap",
                "Day",
                "Born on leap day",
                GenderArch.FEMALE,
                leapDay
            );

            // Then
            assertEquals(leapDay, resource.dob());
        }
    }
}