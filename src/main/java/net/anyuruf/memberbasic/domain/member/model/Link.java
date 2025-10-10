package net.anyuruf.memberbasic.domain.member.model;

import java.util.UUID;

import net.anyuruf.memberbasic.domain.member.model.ParentEnum.Parent;

public record Link(UUID id, UUID source, UUID target, Parent parent) {
}