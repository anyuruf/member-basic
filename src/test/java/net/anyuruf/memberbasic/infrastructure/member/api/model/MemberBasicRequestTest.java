package net.anyuruf.memberbasic.infrastructure.member.api.model;

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

import net.anyuruf.memberbasic.infrastructure.member.api.model.GenderEnumArch.GenderArch;

@DisplayName("MemberBasicRequest Validation Tests")
class MemberBasicRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    @DisplayName("Valid Request Tests")
    class ValidRequestTests {

        @Test
        @DisplayName("Should accept valid request with all fields")
        void shouldAcceptValidRequestWithAllFields() {
            // Given
            MemberBasicRequest request = new MemberBasicRequest(
                "John",
                "Doe",
                "Valid description",
                GenderArch.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            Set<ConstraintViolation<MemberBasicRequest>> violations = validator.validate(request);

            // Then
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Should accept female gender")
        void shouldAcceptFemaleGender() {
            // Given
            MemberBasicRequest request = new MemberBasicRequest(
                "Jane",
                "Smith",
                "Female request",
                GenderArch.FEMALE,
                LocalDate.of(1985, 6, 20)
            );

            // When
            Set<ConstraintViolation<MemberBasicRequest>> violations = validator.validate(request);

            // Then
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("Invalid First Name Tests")
    class InvalidFirstNameTests {

        @Test
        @DisplayName("Should reject null first name")
        void shouldRejectNullFirstName() {
            // Given
            MemberBasicRequest request = new MemberBasicRequest(
                null,
                "Doe",
                "Description",
                GenderArch.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            Set<ConstraintViolation<MemberBasicRequest>> violations = validator.validate(request);

            // Then
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
        }

        @Test
        @DisplayName("Should reject blank first name")
        void shouldRejectBlankFirstName() {
            // Given
            MemberBasicRequest request = new MemberBasicRequest(
                "   ",
                "Doe",
                "Description",
                GenderArch.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            Set<ConstraintViolation<MemberBasicRequest>> violations = validator.validate(request);

            // Then
            assertFalse(violations.isEmpty());
        }

        @Test
        @DisplayName("Should reject empty first name")
        void shouldRejectEmptyFirstName() {
            // Given
            MemberBasicRequest request = new MemberBasicRequest(
                "",
                "Doe",
                "Description",
                GenderArch.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            Set<ConstraintViolation<MemberBasicRequest>> violations = validator.validate(request);

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
            MemberBasicRequest request = new MemberBasicRequest(
                "John",
                null,
                "Description",
                GenderArch.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            Set<ConstraintViolation<MemberBasicRequest>> violations = validator.validate(request);

            // Then
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
        }

        @Test
        @DisplayName("Should reject blank last name")
        void shouldRejectBlankLastName() {
            // Given
            MemberBasicRequest request = new MemberBasicRequest(
                "John",
                "  ",
                "Description",
                GenderArch.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            Set<ConstraintViolation<MemberBasicRequest>> violations = validator.validate(request);

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
            MemberBasicRequest request = new MemberBasicRequest(
                "John",
                "Doe",
                null,
                GenderArch.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            Set<ConstraintViolation<MemberBasicRequest>> violations = validator.validate(request);

            // Then
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("description")));
        }

        @Test
        @DisplayName("Should reject blank description")
        void shouldRejectBlankDescription() {
            // Given
            MemberBasicRequest request = new MemberBasicRequest(
                "John",
                "Doe",
                "  ",
                GenderArch.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            Set<ConstraintViolation<MemberBasicRequest>> violations = validator.validate(request);

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
            MemberBasicRequest request = new MemberBasicRequest(
                "John",
                "Doe",
                "Description",
                GenderArch.MALE,
                null
            );

            // When
            Set<ConstraintViolation<MemberBasicRequest>> violations = validator.validate(request);

            // Then
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("dob")));
        }
    }
}