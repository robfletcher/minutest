package com.oneeyedmen.minutest

import org.junit.jupiter.api.DynamicContainer
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest
import java.util.stream.Stream
import kotlin.streams.asSequence

fun executeTest(tests: Stream<out DynamicNode>) {
    tests.asSequence().forEach { dynamicNode ->
        when (dynamicNode) {
            is DynamicTest -> dynamicNode.executable.execute()
            is DynamicContainer -> executeTest(dynamicNode.children)
        }
    }
}