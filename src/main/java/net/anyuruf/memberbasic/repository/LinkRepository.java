package net.anyuruf.memberbasic.repository;

import java.util.UUID;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import net.anyuruf.memberbasic.entity.Link;

public interface LinkRepository extends ReactiveCassandraRepository<Link, UUID> {
}