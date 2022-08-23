package net.anyuruf.memberbasic.controller;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import net.anyuruf.memberbasic.repository.MemberBasicRepository;
import net.anyuruf.memberbasic.repository.LinkRepository;
import net.anyuruf.memberbasic.entity.Link;
import net.anyuruf.memberbasic.entity.MemberBasic;
import net.anyuruf.memberbasic.entity.MemberBasicInput;

@Controller
public class MemberBasicController {

    private final MemberBasicRepository memberRepository;
    private final LinkRepository linkRepository;

    public MemberBasicController(MemberBasicRepository memRepo, LinkRepository linkRepo) {
        this.memberRepository = memRepo;
        this.linkRepository = linkRepo;
    }

    @QueryMapping
    public Flux<MemberBasic> nodes() {
        return this.memberRepository.findAll();
    }

    @QueryMapping
    public Flux<Link> links() {
        return this.linkRepository.findAll();
    }

    @MutationMapping
    public Mono<MemberBasic> addMember(@Argument MemberBasicInput memberBasicInput) {

        MemberBasic memberBasic = new MemberBasic(memberBasicInput.getFirstName(), memberBasicInput.getLastName(),
                memberBasicInput.getGender(), memberBasicInput.getDob());
        return this.memberRepository.save(memberBasic);
    }

    @MutationMapping
    public Mono<MemberBasic> editMember(@Argument MemberBasic memberBasic) {
        return this.memberRepository.save(memberBasic);
    }

}
