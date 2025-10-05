package net.anyuruf.memberbasic.infrastructure.member.api.model;

import java.util.UUID;

import net.anyuruf.memberbasic.infrastructure.member.spi.entity.ParentEnum.Parent;

public record Link(UUID id, UUID source, UUID target, Parent parent) {
}