package net.anyuruf.memberbasic.infrastructure.member.api.graphql;

import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import lombok.RequiredArgsConstructor;
import net.anyuruf.memberbasic.domain.member.api.MemberApi;
import net.anyuruf.memberbasic.domain.member.entity.GenderEnum.Gender;
import net.anyuruf.memberbasic.domain.member.entity.MemberBasic;
import net.anyuruf.memberbasic.domain.member.entity.MemberBasicInput;
import net.anyuruf.memberbasic.infrastructure.member.api.model.EditMemberRequest;
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
    	 return Flux.from(memberApi.getAllMembers()).map(MemberBasicResource::new);
    }

    @MutationMapping
    public Mono<MemberBasicResource> addMember(@Valid @Argument MemberBasicRequest input) {
        MemberBasicInput member = new MemberBasicInput(
            input.firstName(),
            input.lastName(),
            input.description(),
            Gender.valueOf(input.genderArch().name()),
            input.dob()
        );
        return Mono.from(memberApi.addFamilyMember(member)).map(MemberBasicResource::new);
    }

    @MutationMapping
    public Mono<MemberBasicResource> editMember(@Valid @Argument EditMemberRequest input) {
        MemberBasic member = new MemberBasic(
            input.id(),
            input.firstName(),
            input.lastName(),
            input.description(),
            Gender.valueOf(input.genderArch().name()),
            input.dob()
        );
        return Mono.from(memberApi.editFamilyMember(member)).map(MemberBasicResource::new);
    }

    @QueryMapping
    public Mono<MemberBasicResource> getFamilyMember(@Argument UUID uuid) {
         return Mono.from(memberApi.getFamilyMember(uuid)).map(MemberBasicResource::new);
    }

}
