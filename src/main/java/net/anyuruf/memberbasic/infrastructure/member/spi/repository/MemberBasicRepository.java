package net.anyuruf.memberbasic.infrastructure.member.spi.repository;

import java.util.UUID;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

public interface MemberBasicRepository extends ReactiveCassandraRepository<MemberBasicRepository, UUID> {
}
