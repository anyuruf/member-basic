package net.anyuruf.memberbasic.infrastructure.member.api.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import net.anyuruf.memberbasic.infrastructure.member.api.model.GenderEnumArch.GenderArch;

@DisplayName("EditMemberRequest Validation Tests")
class EditMemberRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    @DisplayName("Valid Edit Request Tests")
    class ValidEditRequestTests {

        @Test
        @DisplayName("Should accept valid edit request with all fields")
        void shouldAcceptValidEditRequestWithAllFields() {
            // Given
            EditMemberRequest request = new EditMemberRequest(
                UUID.randomUUID(),
                "John",
                "Smith",
                "Updated description",
                GenderArch.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            Set<ConstraintViolation<EditMemberRequest>> violations = validator.validate(request);

            // Then
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Should accept female gender in edit request")
        void shouldAcceptFemaleGenderInEditRequest() {
            // Given
            EditMemberRequest request = new EditMemberRequest(
                UUID.randomUUID(),
                "Jane",
                "Doe",
                "Female edit",
                GenderArch.FEMALE,
                LocalDate.of(1985, 6, 20)
            );

            // When
            Set<ConstraintViolation<EditMemberRequest>> violations = validator.validate(request);

            // Then
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("Invalid ID Tests")
    class InvalidIdTests {

        @Test
        @DisplayName("Should reject null ID")
        void shouldRejectNullId() {
            // Given
            EditMemberRequest request = new EditMemberRequest(
                null,
                "John",
                "Doe",
                "Description",
                GenderArch.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            Set<ConstraintViolation<EditMemberRequest>> violations = validator.validate(request);

            // Then
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("id") &&
                              v.getMessage().equals("ID is required for editing")));
        }
    }

    @Nested
    @DisplayName("Invalid First Name Tests")
    class InvalidFirstNameTests {

        @Test
        @DisplayName("Should reject null first name")
        void shouldRejectNullFirstName() {
            // Given
            EditMemberRequest request = new EditMemberRequest(
                UUID.randomUUID(),
                null,
                "Doe",
                "Description",
                GenderArch.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            Set<ConstraintViolation<EditMemberRequest>> violations = validator.validate(request);

            // Then
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
        }

        @Test
        @DisplayName("Should reject blank first name")
        void shouldRejectBlankFirstName() {
            // Given
            EditMemberRequest request = new EditMemberRequest(
                UUID.randomUUID(),
                "   ",
                "Doe",
                "Description",
                GenderArch.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            Set<ConstraintViolation<EditMemberRequest>> violations = validator.validate(request);

            // Then
            assertFalse(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("Invalid Last Name Tests")
    class InvalidLastNameTests {

        @Test
        @DisplayName("Should reject null last name")
        void shouldRejectNullLastName() {
            // Given
            EditMemberRequest request = new EditMemberRequest(
                UUID.randomUUID(),
                "John",
                null,
                "Description",
                GenderArch.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            Set<ConstraintViolation<EditMemberRequest>> violations = validator.validate(request);

            // Then
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
        }

        @Test
        @DisplayName("Should reject blank last name")
        void shouldRejectBlankLastName() {
            // Given
            EditMemberRequest request = new EditMemberRequest(
                UUID.randomUUID(),
                "John",
                "  ",
                "Description",
                GenderArch.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            Set<ConstraintViolation<EditMemberRequest>> violations = validator.validate(request);

            // Then
            assertFalse(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("Invalid Description Tests")
    class InvalidDescriptionTests {

        @Test
        @DisplayName("Should reject null description")
        void shouldRejectNullDescription() {
            // Given
            EditMemberRequest request = new EditMemberRequest(
                UUID.randomUUID(),
                "John",
                "Doe",
                null,
                GenderArch.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            Set<ConstraintViolation<EditMemberRequest>> violations = validator.validate(request);

            // Then
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("description")));
        }

        @Test
        @DisplayName("Should reject blank description")
        void shouldRejectBlankDescription() {
            // Given
            EditMemberRequest request = new EditMemberRequest(
                UUID.randomUUID(),
                "John",
                "Doe",
                "  ",
                GenderArch.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            Set<ConstraintViolation<EditMemberRequest>> violations = validator.validate(request);

            // Then
            assertFalse(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("Invalid Date of Birth Tests")
    class InvalidDateOfBirthTests {

        @Test
        @DisplayName("Should reject null date of birth")
        void shouldRejectNullDateOfBirth() {
            // Given
            EditMemberRequest request = new EditMemberRequest(
                UUID.randomUUID(),
                "John",
                "Doe",
                "Description",
                GenderArch.MALE,
                null
            );

            // When
            Set<ConstraintViolation<EditMemberRequest>> violations = validator.validate(request);

            // Then
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("dob")));
        }
    }

    @Nested
    @DisplayName("Record Properties Tests")
    class RecordPropertiesTests {

        @Test
        @DisplayName("Should be equal when all fields match")
        void shouldBeEqualWhenAllFieldsMatch() {
            // Given
            UUID id = UUID.randomUUID();
            LocalDate dob = LocalDate.of(1990, 1, 15);
            EditMemberRequest request1 = new EditMemberRequest(
                id, "John", "Doe", "Desc", GenderArch.MALE, dob
            );
            EditMemberRequest request2 = new EditMemberRequest(
                id, "John", "Doe", "Desc", GenderArch.MALE, dob
            );

            // Then
            assertEquals(request1, request2);
            assertEquals(request1.hashCode(), request2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when fields differ")
        void shouldNotBeEqualWhenFieldsDiffer() {
            // Given
            LocalDate dob = LocalDate.of(1990, 1, 15);
            EditMemberRequest request1 = new EditMemberRequest(
                UUID.randomUUID(), "John", "Doe", "Desc", GenderArch.MALE, dob
            );
            EditMemberRequest request2 = new EditMemberRequest(
                UUID.randomUUID(), "Jane", "Smith", "Desc", GenderArch.FEMALE, dob
            );

            // Then
            assertNotEquals(request1, request2);
        }
    }
}