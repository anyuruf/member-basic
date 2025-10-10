package net.anyuruf.memberbasic.infrastructure.member.spi.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import net.anyuruf.memberbasic.domain.member.model.GenderEnum.Gender;
import net.anyuruf.memberbasic.domain.member.model.MemberBasic;
import net.anyuruf.memberbasic.domain.member.model.MemberBasicInput;
import net.anyuruf.memberbasic.infrastructure.member.spi.entity.GenderEnumSpi.GenderSpi;

@DisplayName("MemberBasicSpi Entity Tests")
class MemberBasicSpiTest {

    @Nested
    @DisplayName("Constructor from MemberBasicInput Tests")
    class ConstructorFromInputTests {

        @Test
        @DisplayName("Should create SPI entity from domain input with null ID")
        void shouldCreateSpiEntityFromDomainInputWithNullId() {
            // Given
            MemberBasicInput input = new MemberBasicInput(
                "John",
                "Doe",
                "Input description",
                Gender.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            MemberBasicSpi spi = new MemberBasicSpi(input);

            // Then
            assertNull(spi.id(), "ID should be null for new entities");
            assertEquals("John", spi.firstName());
            assertEquals("Doe", spi.lastName());
            assertEquals("Input description", spi.description());
            assertEquals(GenderSpi.MALE, spi.gender());
            assertEquals(LocalDate.of(1990, 1, 15), spi.dob());
        }

        @Test
        @DisplayName("Should convert FEMALE gender from domain input to SPI")
        void shouldConvertFemaleGenderFromDomainInputToSpi() {
            // Given
            MemberBasicInput input = new MemberBasicInput(
                "Jane",
                "Smith",
                "Female input",
                Gender.FEMALE,
                LocalDate.of(1985, 6, 20)
            );

            // When
            MemberBasicSpi spi = new MemberBasicSpi(input);

            // Then
            assertEquals(GenderSpi.FEMALE, spi.gender());
        }

        @Test
        @DisplayName("Should preserve all field values from input")
        void shouldPreserveAllFieldValuesFromInput() {
            // Given
            String firstName = "Jean-Pierre";
            String lastName = "O'Brien-Smith";
            String description = "Special chars: @#$%";
            LocalDate dob = LocalDate.of(2000, 2, 29);
            MemberBasicInput input = new MemberBasicInput(
                firstName, lastName, description, Gender.MALE, dob
            );

            // When
            MemberBasicSpi spi = new MemberBasicSpi(input);

            // Then
            assertEquals(firstName, spi.firstName());
            assertEquals(lastName, spi.lastName());
            assertEquals(description, spi.description());
            assertEquals(dob, spi.dob());
        }
    }

    @Nested
    @DisplayName("Constructor from MemberBasic Tests")
    class ConstructorFromMemberBasicTests {

        @Test
        @DisplayName("Should create SPI entity from domain MemberBasic with ID")
        void shouldCreateSpiEntityFromDomainMemberBasicWithId() {
            // Given
            UUID id = UUID.randomUUID();
            MemberBasic member = new MemberBasic(
                id,
                "John",
                "Doe",
                "Member description",
                Gender.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            MemberBasicSpi spi = new MemberBasicSpi(member);

            // Then
            assertEquals(id, spi.id());
            assertEquals("John", spi.firstName());
            assertEquals("Doe", spi.lastName());
            assertEquals("Member description", spi.description());
            assertEquals(GenderSpi.MALE, spi.gender());
            assertEquals(LocalDate.of(1990, 1, 15), spi.dob());
        }

        @Test
        @DisplayName("Should convert FEMALE gender from domain member to SPI")
        void shouldConvertFemaleGenderFromDomainMemberToSpi() {
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
            MemberBasicSpi spi = new MemberBasicSpi(member);

            // Then
            assertEquals(GenderSpi.FEMALE, spi.gender());
        }

        @Test
        @DisplayName("Should handle null ID in domain member")
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
            MemberBasicSpi spi = new MemberBasicSpi(member);

            // Then
            assertNull(spi.id());
        }
    }

    @Nested
    @DisplayName("ToDomain Conversion Tests")
    class ToDomainConversionTests {

        @Test
        @DisplayName("Should convert SPI entity to domain MemberBasic")
        void shouldConvertSpiEntityToDomainMemberBasic() {
            // Given
            UUID id = UUID.randomUUID();
            LocalDate dob = LocalDate.of(1990, 1, 15);
            MemberBasicSpi spi = new MemberBasicSpi(
                id,
                "John",
                "Doe",
                "SPI description",
                GenderSpi.MALE,
                dob
            );

            // When
            MemberBasic member = spi.toDomain();

            // Then
            assertNotNull(member);
            assertEquals(id, member.id());
            assertEquals("John", member.firstName());
            assertEquals("Doe", member.lastName());
            assertEquals("SPI description", member.description());
            assertEquals(Gender.MALE, member.gender());
            assertEquals(dob, member.dob());
        }

        @Test
        @DisplayName("Should convert FEMALE gender from SPI to domain")
        void shouldConvertFemaleGenderFromSpiToDomain() {
            // Given
            MemberBasicSpi spi = new MemberBasicSpi(
                UUID.randomUUID(),
                "Jane",
                "Smith",
                "Female SPI",
                GenderSpi.FEMALE,
                LocalDate.of(1985, 6, 20)
            );

            // When
            MemberBasic member = spi.toDomain();

            // Then
            assertEquals(Gender.FEMALE, member.gender());
        }

        @Test
        @DisplayName("Should preserve all field values during conversion")
        void shouldPreserveAllFieldValuesDuringConversion() {
            // Given
            UUID id = UUID.randomUUID();
            String firstName = "José";
            String lastName = "Müller";
            String description = "Unicode: 日本語";
            LocalDate dob = LocalDate.of(1992, 3, 15);
            MemberBasicSpi spi = new MemberBasicSpi(
                id, firstName, lastName, description, GenderSpi.MALE, dob
            );

            // When
            MemberBasic member = spi.toDomain();

            // Then
            assertEquals(id, member.id());
            assertEquals(firstName, member.firstName());
            assertEquals(lastName, member.lastName());
            assertEquals(description, member.description());
            assertEquals(dob, member.dob());
        }

        @Test
        @DisplayName("Should handle null ID during conversion")
        void shouldHandleNullIdDuringConversion() {
            // Given
            MemberBasicSpi spi = new MemberBasicSpi(
                null,
                "John",
                "Doe",
                "No ID",
                GenderSpi.MALE,
                LocalDate.now()
            );

            // When
            MemberBasic member = spi.toDomain();

            // Then
            assertNull(member.id());
        }
    }

    @Nested
    @DisplayName("Round-Trip Conversion Tests")
    class RoundTripConversionTests {

        @Test
        @DisplayName("Should maintain data integrity: Input -> SPI -> Domain")
        void shouldMaintainDataIntegrityInputSpiDomain() {
            // Given
            MemberBasicInput originalInput = new MemberBasicInput(
                "John",
                "Doe",
                "Original",
                Gender.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            MemberBasicSpi spi = new MemberBasicSpi(originalInput);
            MemberBasic member = spi.toDomain();

            // Then
            assertEquals(originalInput.firstName(), member.firstName());
            assertEquals(originalInput.lastName(), member.lastName());
            assertEquals(originalInput.description(), member.description());
            assertEquals(originalInput.gender().name(), member.gender().name());
            assertEquals(originalInput.dob(), member.dob());
        }

        @Test
        @DisplayName("Should maintain data integrity: Domain -> SPI -> Domain")
        void shouldMaintainDataIntegrityDomainSpiDomain() {
            // Given
            UUID id = UUID.randomUUID();
            MemberBasic originalMember = new MemberBasic(
                id,
                "Jane",
                "Smith",
                "Original member",
                Gender.FEMALE,
                LocalDate.of(1985, 6, 20)
            );

            // When
            MemberBasicSpi spi = new MemberBasicSpi(originalMember);
            MemberBasic reconvertedMember = spi.toDomain();

            // Then
            assertEquals(originalMember.id(), reconvertedMember.id());
            assertEquals(originalMember.firstName(), reconvertedMember.firstName());
            assertEquals(originalMember.lastName(), reconvertedMember.lastName());
            assertEquals(originalMember.description(), reconvertedMember.description());
            assertEquals(originalMember.gender(), reconvertedMember.gender());
            assertEquals(originalMember.dob(), reconvertedMember.dob());
        }
    }

    @Nested
    @DisplayName("Record Properties Tests")
    class RecordPropertiesTests {

        @Test
        @DisplayName("Should create SPI with direct constructor")
        void shouldCreateSpiWithDirectConstructor() {
            // Given
            UUID id = UUID.randomUUID();
            LocalDate dob = LocalDate.of(1990, 1, 15);

            // When
            MemberBasicSpi spi = new MemberBasicSpi(
                id,
                "John",
                "Doe",
                "Direct",
                GenderSpi.MALE,
                dob
            );

            // Then
            assertEquals(id, spi.id());
            assertEquals("John", spi.firstName());
            assertEquals("Doe", spi.lastName());
            assertEquals("Direct", spi.description());
            assertEquals(GenderSpi.MALE, spi.gender());
            assertEquals(dob, spi.dob());
        }

        @Test
        @DisplayName("Should be equal when all fields match")
        void shouldBeEqualWhenAllFieldsMatch() {
            // Given
            UUID id = UUID.randomUUID();
            LocalDate dob = LocalDate.of(1990, 1, 15);
            MemberBasicSpi spi1 = new MemberBasicSpi(
                id, "John", "Doe", "Desc", GenderSpi.MALE, dob
            );
            MemberBasicSpi spi2 = new MemberBasicSpi(
                id, "John", "Doe", "Desc", GenderSpi.MALE, dob
            );

            // Then
            assertEquals(spi1, spi2);
            assertEquals(spi1.hashCode(), spi2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when fields differ")
        void shouldNotBeEqualWhenFieldsDiffer() {
            // Given
            LocalDate dob = LocalDate.of(1990, 1, 15);
            MemberBasicSpi spi1 = new MemberBasicSpi(
                UUID.randomUUID(), "John", "Doe", "Desc", GenderSpi.MALE, dob
            );
            MemberBasicSpi spi2 = new MemberBasicSpi(
                UUID.randomUUID(), "Jane", "Smith", "Desc", GenderSpi.FEMALE, dob
            );

            // Then
            assertNotEquals(spi1, spi2);
        }

        @Test
        @DisplayName("Should have meaningful toString")
        void shouldHaveMeaningfulToString() {
            // Given
            UUID id = UUID.randomUUID();
            MemberBasicSpi spi = new MemberBasicSpi(
                id,
                "John",
                "Doe",
                "Description",
                GenderSpi.MALE,
                LocalDate.of(1990, 1, 15)
            );

            // When
            String toString = spi.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.contains("John"));
            assertTrue(toString.contains("Doe"));
            assertTrue(toString.contains(id.toString()));
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null values in direct constructor")
        void shouldHandleNullValuesInDirectConstructor() {
            // When
            MemberBasicSpi spi = new MemberBasicSpi(
                null, null, null, null, null, null
            );

            // Then
            assertNull(spi.id());
            assertNull(spi.firstName());
            assertNull(spi.lastName());
            assertNull(spi.description());
            assertNull(spi.gender());
            assertNull(spi.dob());
        }

        @Test
        @DisplayName("Should handle empty strings")
        void shouldHandleEmptyStrings() {
            // When
            MemberBasicSpi spi = new MemberBasicSpi(
                UUID.randomUUID(), "", "", "", GenderSpi.MALE, LocalDate.now()
            );

            // Then
            assertEquals("", spi.firstName());
            assertEquals("", spi.lastName());
            assertEquals("", spi.description());
        }

        @Test
        @DisplayName("Should handle leap year dates")
        void shouldHandleLeapYearDates() {
            // Given
            LocalDate leapDay = LocalDate.of(2000, 2, 29);

            // When
            MemberBasicSpi spi = new MemberBasicSpi(
                UUID.randomUUID(),
                "Leap",
                "Day",
                "Born on leap day",
                GenderSpi.FEMALE,
                leapDay
            );

            // Then
            assertEquals(leapDay, spi.dob());
        }

        @Test
        @DisplayName("Should handle very old dates")
        void shouldHandleVeryOldDates() {
            // Given
            LocalDate oldDate = LocalDate.of(1800, 1, 1);

            // When
            MemberBasicSpi spi = new MemberBasicSpi(
                UUID.randomUUID(),
                "Historical",
                "Person",
                "Very old",
                GenderSpi.MALE,
                oldDate
            );

            // Then
            assertEquals(oldDate, spi.dob());
        }
    }

    @Nested
    @DisplayName("Gender Conversion Tests")
    class GenderConversionTests {

        @Test
        @DisplayName("Should convert all gender values correctly")
        void shouldConvertAllGenderValuesCorrectly() {
            // Test MALE conversion
            MemberBasicInput maleInput = new MemberBasicInput(
                "M", "M", "M", Gender.MALE, LocalDate.now()
            );
            MemberBasicSpi maleSpi = new MemberBasicSpi(maleInput);
            assertEquals(GenderSpi.MALE, maleSpi.gender());
            assertEquals(Gender.MALE, maleSpi.toDomain().gender());

            // Test FEMALE conversion
            MemberBasicInput femaleInput = new MemberBasicInput(
                "F", "F", "F", Gender.FEMALE, LocalDate.now()
            );
            MemberBasicSpi femaleSpi = new MemberBasicSpi(femaleInput);
            assertEquals(GenderSpi.FEMALE, femaleSpi.gender());
            assertEquals(Gender.FEMALE, femaleSpi.toDomain().gender());
        }
    }
}