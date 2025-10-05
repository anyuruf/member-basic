package net.anyuruf.memberbasic.domain.member.entity;

import java.util.UUID;

import net.anyuruf.memberbasic.domain.member.entity.ParentEnum.Parent;

public record Link(UUID id, UUID source, UUID target, Parent parent) {
}