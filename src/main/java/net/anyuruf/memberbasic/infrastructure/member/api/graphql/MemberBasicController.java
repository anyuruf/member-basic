package net.anyuruf.memberbasic.infrastructure.member.api.graphql;

import java.util.UUID;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import net.anyuruf.memberbasic.domain.member.api.MemberApi;
import net.anyuruf.memberbasic.domain.member.model.GenderEnum.Gender;
import net.anyuruf.memberbasic.domain.member.model.MemberBasic;
import net.anyuruf.memberbasic.domain.member.model.MemberBasicInput;
import net.anyuruf.memberbasic.infrastructure.member.api.model.EditMemberRequest;
import net.anyuruf.memberbasic.infrastructure.member.api.model.MemberBasicRequest;
import net.anyuruf.memberbasic.infrastructure.member.api.model.MemberBasicResource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Controller
@Validated
public class MemberBasicController {

    private final MemberApi memberApi;
    
    public MemberBasicController (MemberApi memberApi) {
    	this.memberApi = memberApi;
    }

    @QueryMapping
    public Flux<MemberBasicResource> getAllMembers() {
    	 return memberApi.getAllMembers().map(MemberBasicResource::new);
    }

    @MutationMapping
    public Mono<MemberBasicResource> addMember(@Valid @Argument MemberBasicRequest memberBasicRequest) {
        MemberBasicInput member = new MemberBasicInput(
            memberBasicRequest.firstName(),
            memberBasicRequest.lastName(),
            memberBasicRequest.description(),
            Gender.valueOf(memberBasicRequest.genderArch().name()),
            memberBasicRequest.dob()
        );
        return memberApi.addFamilyMember(member).map(MemberBasicResource::new);
    }

    @MutationMapping
    public Mono<MemberBasicResource> editMember(@Valid @Argument EditMemberRequest editMemberRequest) {
        MemberBasic member = new MemberBasic(
            editMemberRequest.id(),
            editMemberRequest.firstName(),
            editMemberRequest.lastName(),
            editMemberRequest.description(),
            Gender.valueOf(editMemberRequest.genderArch().name()),
            editMemberRequest.dob()
        );
        return memberApi.editFamilyMember(member).map(MemberBasicResource::new);
    }

    @QueryMapping
    public Mono<MemberBasicResource> getFamilyMember(@Argument UUID id) {
         return memberApi.getFamilyMember(id).map(MemberBasicResource::new);
    }

}
