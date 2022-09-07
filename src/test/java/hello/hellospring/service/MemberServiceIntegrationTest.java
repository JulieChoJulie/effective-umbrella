package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class MemberServiceIntegrationTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    void join() {
        // given
        Member member = new Member();
        member.setName("spring");

        // when
        Long savedId = memberService.join(member);

        // then
        Member foundMember = memberService.findOne(savedId).get();
        assertThat(member.getName()).isEqualTo(foundMember.getName());
    }

    @Test
    public void duplicate_member_exception() {
        //given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        //when
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        assertThat(e.getMessage()).isEqualTo("The name is already in use.");
    }

    @Test
    void findMembers() {
        //given
        Member member1= new Member();
        member1.setName("spring");
        memberService.join(member1);

        Member member2= new Member();
        member2.setName("spring2");
        memberService.join(member2);

        // when
        List<Member> allMembers = memberService.findMembers();

        // then
        assertThat(allMembers.size()).isEqualTo(2);

    }

    @Test
    void findOne() {
        //given
        Member member1= new Member();
        member1.setName("spring");
        memberService.join(member1);

        Member member2= new Member();
        member2.setName("spring2");
        memberService.join(member2);

        // when
        Member result = memberService.findOne(member1.getId()).get();

        // then
        assertThat(result).isEqualTo(member1);
    }
}
