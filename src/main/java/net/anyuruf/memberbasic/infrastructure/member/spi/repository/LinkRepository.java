package net.anyuruf.memberbasic.infrastructure.member.spi.repository;

import java.util.UUID;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import net.anyuruf.memberbasic.infrastructure.member.spi.entity.Link;

public interface LinkRepository extends ReactiveCassandraRepository<Link, UUID> {
}