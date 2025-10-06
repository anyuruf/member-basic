package net.anyuruf.memberbasic.infrastructure.member.api.graphql;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;
import net.anyuruf.memberbasic.domain.member.api.MemberApi;
import net.anyuruf.memberbasic.domain.member.entity.MemberBasic;
import net.anyuruf.memberbasic.infrastructure.member.api.model.EditMemberRequest;
import net.anyuruf.memberbasic.infrastructure.member.api.model.MemberBasicInput;
import net.anyuruf.memberbasic.infrastructure.member.api.model.MemberBasicRequest;
import net.anyuruf.memberbasic.infrastructure.member.api.model.MemberBasicResource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Controller
@Validated
@RequiredArgsConstructor
public class MemberBasicController {

    private final MemberApi memberApi;

    @QueryMapping
    public Flux<MemberBasicResource> getAllMembers() {
        return Flux.from(memberApi.getAllMembers());
    }

    @MutationMapping
    public Mono<MemberBasicResource> addMember(@Valid @Argument MemberBasicInput input) {
        MemberBasic member = new MemberBasic(
            null,
            input.firstName(),
            input.lastName(),
            input.description(),
            input.gender(),
            input.dob()
        );
        return Mono.from(memberApi.addFamilyMember(member));
    }

    @MutationMapping
    public Mono<MemberBasicResource> editMember(@Valid @Argument EditMemberRequest input) {
        MemberBasic member = new MemberBasic(
            input.id(),
            input.firstName(),
            input.lastName(),
            input.description(),
            input.gender(),
            input.dob()
        );
        return Mono.from(memberApi.editFamilyMember(member));
    }

    @QueryMapping
    public Mono<MemberBasicResource> getFamilyMember(@PathVariable UUID uuid) {
         return memberApi.getFamilyMember(uuid);
    }

}
