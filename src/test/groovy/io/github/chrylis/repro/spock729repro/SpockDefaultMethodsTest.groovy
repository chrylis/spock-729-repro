package io.github.chrylis.repro.spock729repro

import static io.github.chrylis.repro.spock729repro.MockMe.ONE
import static java.util.UUID.randomUUID

import spock.lang.Specification

class SpockDefaultMethodsTest extends Specification {

    /* Using Mock() **************************************************/

    MockMe mock = Mock() {
        equalsOne(_ as UUID) >> { ONE == it[0] }
    }

    def 'calling mocked method directly succeeds'() {
        expect:
        mock.equalsOne(ONE)
        ! mock.equalsOne(randomUUID())
    }


    def 'delegating to mocked method via default implementation succeeds with Mock'() {
        expect: "Since an actual implementation is provided, we don't overwrite it"
        mock.equalsOneFromString('00000000-0000-0000-0000-000000000001')

        and:
        ! mock.equalsOneFromString('a40f15ac-5351-4254-b78f-95d82aac205d')
    }


    MockMe mockWithExplicitRealCall = Mock() {
        equalsOne(_ as UUID) >> { ONE == it[0] }
        equalsOneFromString(_ as String) >> { callRealMethod() }
    }

    def 'explicit callRealMethod() succeeds with Mock'() {
        when: "Okay, at least callRealMethod() will invoke the default and use equalsOne(_)"
        assert mock.equalsOneFromString('00000000-0000-0000-0000-000000000001')

        then: "we can use cardinality on the mock"
        1 * mock.equalsOne(ONE)
    }



    /* Using Stub() **************************************************/

    MockMe stub = Stub() {
        equalsOne(_ as UUID) >> { ONE == it[0] }
    }

    def 'delegating to mocked method via default implementation succeeds with Stub'() {
        when: "At least Stub, with its 'ambitious' defaults, preserves the default implementation"
        "NARRATOR: No, this fails"
        assert stub.equalsOneFromString('00000000-0000-0000-0000-000000000001')

        then: "And oh no, we can't assert cardinality for the underlying interface"
        /* 1 * */ stub.equalsOne(ONE) >> { ONE == it[0] }
    }


    MockMe stubWithExplicitRealCall = Stub() {
        equalsOne(_ as UUID) >> { ONE == it[0] }
        equalsOneFromString(_ as String) >> { callRealMethod() }
    }

    def 'explicit callRealMethod() succeeds with Stub'() {
        when: "Finally, this version does work!"
        assert stubWithExplicitRealCall.equalsOneFromString('00000000-0000-0000-0000-000000000001')

        then: "But we still can't assert cardinality, even with an in-test interaction"
        /* 1 * */ stubWithExplicitRealCall.equalsOne(ONE) >> true
    }


    /* Using Spy() ***************************************************/

    MockMe spy = Spy() {
        equalsOne(_ as UUID) >> { ONE == it[0] }
    }

    def 'delegating to mocked method via default implementation succeeds with Spy'() {
        when: "Since Spy is the Real Thing, this should work, right?"
        "Note that this seems to invoke callRealMethod() the same way as in an explicit interaction"
        assert spy.equalsOneFromString('00000000-0000-0000-0000-000000000001')

        then:
        1 * spy.equalsOne(ONE)
    }


    MockMe spyWithExplicitRealCall = Spy() {
        equalsOne(_ as UUID) >> { ONE == it[0] }
        equalsOneFromString(_ as String) >> { callRealMethod() }
    }

    def 'explicit callRealMethod() succeeds with Spy'() {
        when: "Okay, we'll try callRealMethod() explicitly, but it escapes the mock infrastructure"
        assert spyWithExplicitRealCall.equalsOneFromString('00000000-0000-0000-0000-000000000001')

        then:
        1 * spyWithExplicitRealCall.equalsOne(ONE)
    }
}
