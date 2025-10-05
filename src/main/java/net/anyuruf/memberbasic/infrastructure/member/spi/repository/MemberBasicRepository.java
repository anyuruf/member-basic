package net.anyuruf.memberbasic.infrastructure.member.spi.repository;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import java.util.UUID;
import net.anyuruf.memberbasic.entity.MemberBasic;

public interface MemberBasicRepository extends ReactiveCassandraRepository<MemberBasic, UUID> {
}
