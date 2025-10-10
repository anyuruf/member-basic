package net.anyuruf.memberbasic.domain.member.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import net.anyuruf.memberbasic.domain.member.model.GenderEnum.Gender;

@DisplayName("MemberBasicInput Validation Tests")
class MemberBasicInputTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    @DisplayName("Valid MemberBasicInput Tests")
    class ValidInputTests {

        @Test
        @DisplayName("Should create valid MemberBasicInput with all required fields")
        void shouldCreateValidMemberBasicInput() {
            // Given
            MemberBasicInput input = new MemberBasicInput(
                "John",
                "Doe",
                "A family member",
                Gender.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            Set<ConstraintViolation<MemberBasicInput>> violations = validator.validate(input);

            // Then
            assertTrue(violations.isEmpty(), "Should have no validation errors");
            assertEquals("John", input.firstName());
            assertEquals("Doe", input.lastName());
            assertEquals("A family member", input.description());
            assertEquals(Gender.MALE, input.gender());
            assertEquals(LocalDate.of(1990, 1, 15), input.dob());
        }

        @Test
        @DisplayName("Should accept female gender")
        void shouldAcceptFemaleGender() {
            // Given
            MemberBasicInput input = new MemberBasicInput(
                "Jane",
                "Smith",
                "Another family member",
                Gender.FEMALE,
                LocalDate.of(1985, 6, 20)
            );

            // When
            Set<ConstraintViolation<MemberBasicInput>> violations = validator.validate(input);

            // Then
            assertTrue(violations.isEmpty());
            assertEquals(Gender.FEMALE, input.gender());
        }

        @Test
        @DisplayName("Should accept date of birth in the past")
        void shouldAcceptPastDateOfBirth() {
            // Given
            LocalDate pastDate = LocalDate.now().minusYears(50);
            MemberBasicInput input = new MemberBasicInput(
                "Historical",
                "Person",
                "Old family member",
                Gender.MALE,
                pastDate
            );

            // When
            Set<ConstraintViolation<MemberBasicInput>> violations = validator.validate(input);

            // Then
            assertTrue(violations.isEmpty());
            assertEquals(pastDate, input.dob());
        }

        @Test
        @DisplayName("Should accept date of birth today")
        void shouldAcceptTodayAsDateOfBirth() {
            // Given
            LocalDate today = LocalDate.now();
            MemberBasicInput input = new MemberBasicInput(
                "New",
                "Born",
                "Newborn family member",
                Gender.FEMALE,
                today
            );

            // When
            Set<ConstraintViolation<MemberBasicInput>> violations = validator.validate(input);

            // Then
            assertTrue(violations.isEmpty());
            assertEquals(today, input.dob());
        }
    }

    @Nested
    @DisplayName("Invalid MemberBasicInput Tests - First Name")
    class InvalidFirstNameTests {

        @Test
        @DisplayName("Should reject null first name")
        void shouldRejectNullFirstName() {
            // Given
            MemberBasicInput input = new MemberBasicInput(
                null,
                "Doe",
                "Description",
                Gender.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            Set<ConstraintViolation<MemberBasicInput>> violations = validator.validate(input);

            // Then
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("firstName") &&
                              v.getMessage().equals("First name is required")));
        }

        @Test
        @DisplayName("Should reject blank first name")
        void shouldRejectBlankFirstName() {
            // Given
            MemberBasicInput input = new MemberBasicInput(
                "   ",
                "Doe",
                "Description",
                Gender.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            Set<ConstraintViolation<MemberBasicInput>> violations = validator.validate(input);

            // Then
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
        }

        @Test
        @DisplayName("Should reject empty first name")
        void shouldRejectEmptyFirstName() {
            // Given
            MemberBasicInput input = new MemberBasicInput(
                "",
                "Doe",
                "Description",
                Gender.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            Set<ConstraintViolation<MemberBasicInput>> violations = validator.validate(input);

            // Then
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
        }
    }

    @Nested
    @DisplayName("Invalid MemberBasicInput Tests - Last Name")
    class InvalidLastNameTests {

        @Test
        @DisplayName("Should reject null last name")
        void shouldRejectNullLastName() {
            // Given
            MemberBasicInput input = new MemberBasicInput(
                "John",
                null,
                "Description",
                Gender.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            Set<ConstraintViolation<MemberBasicInput>> violations = validator.validate(input);

            // Then
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("lastName") &&
                              v.getMessage().equals("Last name is required")));
        }

        @Test
        @DisplayName("Should reject blank last name")
        void shouldRejectBlankLastName() {
            // Given
            MemberBasicInput input = new MemberBasicInput(
                "John",
                "   ",
                "Description",
                Gender.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            Set<ConstraintViolation<MemberBasicInput>> violations = validator.validate(input);

            // Then
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
        }
    }

    @Nested
    @DisplayName("Invalid MemberBasicInput Tests - Description")
    class InvalidDescriptionTests {

        @Test
        @DisplayName("Should reject null description")
        void shouldRejectNullDescription() {
            // Given
            MemberBasicInput input = new MemberBasicInput(
                "John",
                "Doe",
                null,
                Gender.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            Set<ConstraintViolation<MemberBasicInput>> violations = validator.validate(input);

            // Then
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("description") &&
                              v.getMessage().equals("Description is required")));
        }

        @Test
        @DisplayName("Should reject blank description")
        void shouldRejectBlankDescription() {
            // Given
            MemberBasicInput input = new MemberBasicInput(
                "John",
                "Doe",
                "  ",
                Gender.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            Set<ConstraintViolation<MemberBasicInput>> violations = validator.validate(input);

            // Then
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("description")));
        }
    }

    @Nested
    @DisplayName("Invalid MemberBasicInput Tests - Date of Birth")
    class InvalidDateOfBirthTests {

        @Test
        @DisplayName("Should reject null date of birth")
        void shouldRejectNullDateOfBirth() {
            // Given
            MemberBasicInput input = new MemberBasicInput(
                "John",
                "Doe",
                "Description",
                Gender.MALE,
                null
            );

            // When
            Set<ConstraintViolation<MemberBasicInput>> violations = validator.validate(input);

            // Then
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("dob") &&
                              v.getMessage().equals("Date of birth is required")));
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle single character names")
        void shouldHandleSingleCharacterNames() {
            // Given
            MemberBasicInput input = new MemberBasicInput(
                "X",
                "Y",
                "Single char names",
                Gender.MALE,
                LocalDate.of(2000, 1, 1)
            );

            // When
            Set<ConstraintViolation<MemberBasicInput>> violations = validator.validate(input);

            // Then
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Should handle very long names")
        void shouldHandleVeryLongNames() {
            // Given
            String longName = "A".repeat(100);
            MemberBasicInput input = new MemberBasicInput(
                longName,
                longName,
                "Very long names test",
                Gender.FEMALE,
                LocalDate.of(1995, 12, 31)
            );

            // When
            Set<ConstraintViolation<MemberBasicInput>> violations = validator.validate(input);

            // Then
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Should handle special characters in names")
        void shouldHandleSpecialCharactersInNames() {
            // Given
            MemberBasicInput input = new MemberBasicInput(
                "Jean-Pierre",
                "O'Brien",
                "Name with apostrophe and hyphen",
                Gender.MALE,
                LocalDate.of(1988, 7, 4)
            );

            // When
            Set<ConstraintViolation<MemberBasicInput>> violations = validator.validate(input);

            // Then
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Should handle Unicode characters in names")
        void shouldHandleUnicodeCharactersInNames() {
            // Given
            MemberBasicInput input = new MemberBasicInput(
                "José",
                "Müller",
                "Unicode test: 日本語",
                Gender.MALE,
                LocalDate.of(1992, 3, 15)
            );

            // When
            Set<ConstraintViolation<MemberBasicInput>> violations = validator.validate(input);

            // Then
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Should handle leap year date of birth")
        void shouldHandleLeapYearDateOfBirth() {
            // Given
            MemberBasicInput input = new MemberBasicInput(
                "Leap",
                "Year",
                "Born on leap day",
                Gender.FEMALE,
                LocalDate.of(2000, 2, 29)
            );

            // When
            Set<ConstraintViolation<MemberBasicInput>> violations = validator.validate(input);

            // Then
            assertTrue(violations.isEmpty());
            assertEquals(LocalDate.of(2000, 2, 29), input.dob());
        }

        @Test
        @DisplayName("Should handle very old dates")
        void shouldHandleVeryOldDates() {
            // Given
            MemberBasicInput input = new MemberBasicInput(
                "Ancient",
                "Person",
                "Very old date",
                Gender.MALE,
                LocalDate.of(1800, 1, 1)
            );

            // When
            Set<ConstraintViolation<MemberBasicInput>> violations = validator.validate(input);

            // Then
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("Record Equality and HashCode Tests")
    class RecordEqualityTests {

        @Test
        @DisplayName("Should be equal when all fields match")
        void shouldBeEqualWhenAllFieldsMatch() {
            // Given
            MemberBasicInput input1 = new MemberBasicInput(
                "John",
                "Doe",
                "Description",
                Gender.MALE,
                LocalDate.of(1990, 1, 15)
            );
            MemberBasicInput input2 = new MemberBasicInput(
                "John",
                "Doe",
                "Description",
                Gender.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // Then
            assertEquals(input1, input2);
            assertEquals(input1.hashCode(), input2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when fields differ")
        void shouldNotBeEqualWhenFieldsDiffer() {
            // Given
            MemberBasicInput input1 = new MemberBasicInput(
                "John",
                "Doe",
                "Description",
                Gender.MALE,
                LocalDate.of(1990, 1, 15)
            );
            MemberBasicInput input2 = new MemberBasicInput(
                "Jane",
                "Doe",
                "Description",
                Gender.FEMALE,
                LocalDate.of(1990, 1, 15)
            );

            // Then
            assertNotEquals(input1, input2);
        }

        @Test
        @DisplayName("Should have consistent toString")
        void shouldHaveConsistentToString() {
            // Given
            MemberBasicInput input = new MemberBasicInput(
                "John",
                "Doe",
                "Description",
                Gender.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            String toString = input.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.contains("John"));
            assertTrue(toString.contains("Doe"));
            assertTrue(toString.contains("MALE"));
        }
    }
}