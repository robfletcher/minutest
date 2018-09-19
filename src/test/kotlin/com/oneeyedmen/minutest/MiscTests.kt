package com.oneeyedmen.minutest

import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.TestFactory


object MiscTests {

    @TestFactory fun `assume works`() = context<Unit> {

        test("try it") {
            @Suppress("SimplifyBooleanWithConstants")
            assumeTrue("black" == "white")
            fail("shouldn't get here")
            // and, more to the point, the test runner shows a skipped test
        }
    }
}