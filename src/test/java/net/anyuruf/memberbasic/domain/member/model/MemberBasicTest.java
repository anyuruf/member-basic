package net.anyuruf.memberbasic.domain.member.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import net.anyuruf.memberbasic.domain.member.model.GenderEnum.Gender;

@DisplayName("MemberBasic Record Tests")
class MemberBasicTest {

    @Nested
    @DisplayName("Record Creation Tests")
    class RecordCreationTests {

        @Test
        @DisplayName("Should create MemberBasic with all fields")
        void shouldCreateMemberBasicWithAllFields() {
            // Given
            UUID id = UUID.randomUUID();
            String firstName = "John";
            String lastName = "Doe";
            String description = "Family member";
            Gender gender = Gender.MALE;
            LocalDate dob = LocalDate.of(1990, 5, 15);

            // When
            MemberBasic member = new MemberBasic(id, firstName, lastName, description, gender, dob);

            // Then
            assertNotNull(member);
            assertEquals(id, member.id());
            assertEquals(firstName, member.firstName());
            assertEquals(lastName, member.lastName());
            assertEquals(description, member.description());
            assertEquals(gender, member.gender());
            assertEquals(dob, member.dob());
        }

        @Test
        @DisplayName("Should create MemberBasic with null id")
        void shouldCreateMemberBasicWithNullId() {
            // When
            MemberBasic member = new MemberBasic(
                null,
                "John",
                "Doe",
                "Description",
                Gender.MALE,
                LocalDate.of(1990, 1, 1)
            );

            // Then
            assertNull(member.id());
            assertEquals("John", member.firstName());
        }

        @Test
        @DisplayName("Should create MemberBasic with female gender")
        void shouldCreateMemberBasicWithFemaleGender() {
            // When
            MemberBasic member = new MemberBasic(
                UUID.randomUUID(),
                "Jane",
                "Smith",
                "Female family member",
                Gender.FEMALE,
                LocalDate.of(1985, 3, 20)
            );

            // Then
            assertEquals(Gender.FEMALE, member.gender());
        }
    }

    @Nested
    @DisplayName("Record Equality Tests")
    class RecordEqualityTests {

        @Test
        @DisplayName("Should be equal when all fields match")
        void shouldBeEqualWhenAllFieldsMatch() {
            // Given
            UUID id = UUID.randomUUID();
            LocalDate dob = LocalDate.of(1990, 5, 15);
            
            MemberBasic member1 = new MemberBasic(id, "John", "Doe", "Description", Gender.MALE, dob);
            MemberBasic member2 = new MemberBasic(id, "John", "Doe", "Description", Gender.MALE, dob);

            // Then
            assertEquals(member1, member2);
            assertEquals(member1.hashCode(), member2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when ids differ")
        void shouldNotBeEqualWhenIdsDiffer() {
            // Given
            LocalDate dob = LocalDate.of(1990, 5, 15);
            MemberBasic member1 = new MemberBasic(UUID.randomUUID(), "John", "Doe", "Description", Gender.MALE, dob);
            MemberBasic member2 = new MemberBasic(UUID.randomUUID(), "John", "Doe", "Description", Gender.MALE, dob);

            // Then
            assertNotEquals(member1, member2);
        }

        @Test
        @DisplayName("Should not be equal when names differ")
        void shouldNotBeEqualWhenNamesDiffer() {
            // Given
            UUID id = UUID.randomUUID();
            LocalDate dob = LocalDate.of(1990, 5, 15);
            MemberBasic member1 = new MemberBasic(id, "John", "Doe", "Description", Gender.MALE, dob);
            MemberBasic member2 = new MemberBasic(id, "Jane", "Doe", "Description", Gender.MALE, dob);

            // Then
            assertNotEquals(member1, member2);
        }

        @Test
        @DisplayName("Should not be equal when dates differ")
        void shouldNotBeEqualWhenDatesDiffer() {
            // Given
            UUID id = UUID.randomUUID();
            MemberBasic member1 = new MemberBasic(id, "John", "Doe", "Description", Gender.MALE, LocalDate.of(1990, 1, 1));
            MemberBasic member2 = new MemberBasic(id, "John", "Doe", "Description", Gender.MALE, LocalDate.of(1990, 1, 2));

            // Then
            assertNotEquals(member1, member2);
        }

        @Test
        @DisplayName("Should not be equal when genders differ")
        void shouldNotBeEqualWhenGendersDiffer() {
            // Given
            UUID id = UUID.randomUUID();
            LocalDate dob = LocalDate.of(1990, 5, 15);
            MemberBasic member1 = new MemberBasic(id, "Person", "Name", "Description", Gender.MALE, dob);
            MemberBasic member2 = new MemberBasic(id, "Person", "Name", "Description", Gender.FEMALE, dob);

            // Then
            assertNotEquals(member1, member2);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should have meaningful toString representation")
        void shouldHaveMeaningfulToString() {
            // Given
            UUID id = UUID.randomUUID();
            MemberBasic member = new MemberBasic(
                id,
                "John",
                "Doe",
                "Test description",
                Gender.MALE,
                LocalDate.of(1990, 5, 15)
            );

            // When
            String toString = member.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.contains("John"));
            assertTrue(toString.contains("Doe"));
            assertTrue(toString.contains(id.toString()));
            assertTrue(toString.contains("MALE"));
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null firstName")
        void shouldHandleNullFirstName() {
            // When
            MemberBasic member = new MemberBasic(
                UUID.randomUUID(),
                null,
                "Doe",
                "Description",
                Gender.MALE,
                LocalDate.of(1990, 1, 1)
            );

            // Then
            assertNull(member.firstName());
        }

        @Test
        @DisplayName("Should handle empty strings")
        void shouldHandleEmptyStrings() {
            // When
            MemberBasic member = new MemberBasic(
                UUID.randomUUID(),
                "",
                "",
                "",
                Gender.MALE,
                LocalDate.of(1990, 1, 1)
            );

            // Then
            assertEquals("", member.firstName());
            assertEquals("", member.lastName());
            assertEquals("", member.description());
        }

        @Test
        @DisplayName("Should handle special characters")
        void shouldHandleSpecialCharacters() {
            // Given
            String specialName = "O'Brien-Smith";
            String specialDescription = "Special chars: @#$%^&*()";

            // When
            MemberBasic member = new MemberBasic(
                UUID.randomUUID(),
                specialName,
                specialName,
                specialDescription,
                Gender.MALE,
                LocalDate.of(1990, 1, 1)
            );

            // Then
            assertEquals(specialName, member.firstName());
            assertEquals(specialDescription, member.description());
        }

        @Test
        @DisplayName("Should handle leap year dates")
        void shouldHandleLeapYearDates() {
            // When
            MemberBasic member = new MemberBasic(
                UUID.randomUUID(),
                "Leap",
                "Year",
                "Born on leap day",
                Gender.FEMALE,
                LocalDate.of(2000, 2, 29)
            );

            // Then
            assertEquals(LocalDate.of(2000, 2, 29), member.dob());
        }

        @Test
        @DisplayName("Should handle very old dates")
        void shouldHandleVeryOldDates() {
            // When
            MemberBasic member = new MemberBasic(
                UUID.randomUUID(),
                "Historical",
                "Person",
                "Ancient date",
                Gender.MALE,
                LocalDate.of(1500, 1, 1)
            );

            // Then
            assertEquals(LocalDate.of(1500, 1, 1), member.dob());
        }

        @Test
        @DisplayName("Should handle null gender")
        void shouldHandleNullGender() {
            // When
            MemberBasic member = new MemberBasic(
                UUID.randomUUID(),
                "Person",
                "Name",
                "No gender specified",
                null,
                LocalDate.of(1990, 1, 1)
            );

            // Then
            assertNull(member.gender());
        }
    }
}