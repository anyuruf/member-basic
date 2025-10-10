package net.anyuruf.memberbasic.domain.member.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import net.anyuruf.memberbasic.domain.member.model.ParentEnum.Parent;

@DisplayName("Link Record Tests")
class LinkTest {

    @Nested
    @DisplayName("Link Creation Tests")
    class LinkCreationTests {

        @Test
        @DisplayName("Should create Link with all fields - Father")
        void shouldCreateLinkWithFatherRelation() {
            // Given
            UUID id = UUID.randomUUID();
            UUID source = UUID.randomUUID();
            UUID target = UUID.randomUUID();
            Parent parent = Parent.FATHER;

            // When
            Link link = new Link(id, source, target, parent);

            // Then
            assertNotNull(link);
            assertEquals(id, link.id());
            assertEquals(source, link.source());
            assertEquals(target, link.target());
            assertEquals(Parent.FATHER, link.parent());
        }

        @Test
        @DisplayName("Should create Link with all fields - Mother")
        void shouldCreateLinkWithMotherRelation() {
            // Given
            UUID id = UUID.randomUUID();
            UUID source = UUID.randomUUID();
            UUID target = UUID.randomUUID();
            Parent parent = Parent.MOTHER;

            // When
            Link link = new Link(id, source, target, parent);

            // Then
            assertEquals(Parent.MOTHER, link.parent());
        }

        @Test
        @DisplayName("Should create Link with null id")
        void shouldCreateLinkWithNullId() {
            // When
            Link link = new Link(null, UUID.randomUUID(), UUID.randomUUID(), Parent.FATHER);

            // Then
            assertNull(link.id());
        }

        @Test
        @DisplayName("Should allow same UUID for source and target")
        void shouldAllowSameUuidForSourceAndTarget() {
            // Given
            UUID sameId = UUID.randomUUID();

            // When
            Link link = new Link(UUID.randomUUID(), sameId, sameId, Parent.FATHER);

            // Then
            assertEquals(link.source(), link.target());
        }
    }

    @Nested
    @DisplayName("Link Equality Tests")
    class LinkEqualityTests {

        @Test
        @DisplayName("Should be equal when all fields match")
        void shouldBeEqualWhenAllFieldsMatch() {
            // Given
            UUID id = UUID.randomUUID();
            UUID source = UUID.randomUUID();
            UUID target = UUID.randomUUID();
            
            Link link1 = new Link(id, source, target, Parent.FATHER);
            Link link2 = new Link(id, source, target, Parent.FATHER);

            // Then
            assertEquals(link1, link2);
            assertEquals(link1.hashCode(), link2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when ids differ")
        void shouldNotBeEqualWhenIdsDiffer() {
            // Given
            UUID source = UUID.randomUUID();
            UUID target = UUID.randomUUID();
            
            Link link1 = new Link(UUID.randomUUID(), source, target, Parent.FATHER);
            Link link2 = new Link(UUID.randomUUID(), source, target, Parent.FATHER);

            // Then
            assertNotEquals(link1, link2);
        }

        @Test
        @DisplayName("Should not be equal when sources differ")
        void shouldNotBeEqualWhenSourcesDiffer() {
            // Given
            UUID id = UUID.randomUUID();
            UUID target = UUID.randomUUID();
            
            Link link1 = new Link(id, UUID.randomUUID(), target, Parent.FATHER);
            Link link2 = new Link(id, UUID.randomUUID(), target, Parent.FATHER);

            // Then
            assertNotEquals(link1, link2);
        }

        @Test
        @DisplayName("Should not be equal when targets differ")
        void shouldNotBeEqualWhenTargetsDiffer() {
            // Given
            UUID id = UUID.randomUUID();
            UUID source = UUID.randomUUID();
            
            Link link1 = new Link(id, source, UUID.randomUUID(), Parent.FATHER);
            Link link2 = new Link(id, source, UUID.randomUUID(), Parent.FATHER);

            // Then
            assertNotEquals(link1, link2);
        }

        @Test
        @DisplayName("Should not be equal when parent types differ")
        void shouldNotBeEqualWhenParentTypesDiffer() {
            // Given
            UUID id = UUID.randomUUID();
            UUID source = UUID.randomUUID();
            UUID target = UUID.randomUUID();
            
            Link link1 = new Link(id, source, target, Parent.FATHER);
            Link link2 = new Link(id, source, target, Parent.MOTHER);

            // Then
            assertNotEquals(link1, link2);
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
            UUID source = UUID.randomUUID();
            UUID target = UUID.randomUUID();
            Link link = new Link(id, source, target, Parent.FATHER);

            // When
            String toString = link.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.contains(id.toString()));
            assertTrue(toString.contains(source.toString()));
            assertTrue(toString.contains(target.toString()));
            assertTrue(toString.contains("FATHER"));
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null parent")
        void shouldHandleNullParent() {
            // When
            Link link = new Link(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), null);

            // Then
            assertNull(link.parent());
        }

        @Test
        @DisplayName("Should handle null source")
        void shouldHandleNullSource() {
            // When
            Link link = new Link(UUID.randomUUID(), null, UUID.randomUUID(), Parent.FATHER);

            // Then
            assertNull(link.source());
        }

        @Test
        @DisplayName("Should handle null target")
        void shouldHandleNullTarget() {
            // When
            Link link = new Link(UUID.randomUUID(), UUID.randomUUID(), null, Parent.MOTHER);

            // Then
            assertNull(link.target());
        }

        @Test
        @DisplayName("Should handle all null values")
        void shouldHandleAllNullValues() {
            // When
            Link link = new Link(null, null, null, null);

            // Then
            assertNull(link.id());
            assertNull(link.source());
            assertNull(link.target());
            assertNull(link.parent());
        }
    }

    @Nested
    @DisplayName("Parent Enum Tests")
    class ParentEnumTests {

        @Test
        @DisplayName("Should have FATHER value")
        void shouldHaveFatherValue() {
            // Then
            assertNotNull(Parent.FATHER);
            assertEquals("FATHER", Parent.FATHER.name());
        }

        @Test
        @DisplayName("Should have MOTHER value")
        void shouldHaveMotherValue() {
            // Then
            assertNotNull(Parent.MOTHER);
            assertEquals("MOTHER", Parent.MOTHER.name());
        }

        @Test
        @DisplayName("Should have exactly two values")
        void shouldHaveExactlyTwoValues() {
            // Then
            assertEquals(2, Parent.values().length);
        }

        @Test
        @DisplayName("Should support valueOf")
        void shouldSupportValueOf() {
            // Then
            assertEquals(Parent.FATHER, Parent.valueOf("FATHER"));
            assertEquals(Parent.MOTHER, Parent.valueOf("MOTHER"));
        }
    }
}