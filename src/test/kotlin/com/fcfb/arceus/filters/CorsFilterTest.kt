package com.fcfb.arceus.filters

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class CorsFilterTest {
    private lateinit var corsFilter: CorsFilter

    @BeforeEach
    fun setup() {
        corsFilter = CorsFilter()
    }

    @Test
    fun `CorsFilter should be properly annotated`() {
        val componentAnnotation = CorsFilter::class.annotations.find { it is org.springframework.stereotype.Component }
        assertNotNull(componentAnnotation, "CorsFilter should be annotated with @Component")
    }

    @Test
    fun `CorsFilter should be instantiable`() {
        val filter = CorsFilter()
        assertNotNull(filter, "CorsFilter should be instantiable")
    }

    @Test
    fun `CorsFilter should have doFilterInternal method`() {
        val methods = CorsFilter::class.java.declaredMethods
        val doFilterInternalMethod = methods.find { it.name == "doFilterInternal" }
        assertNotNull(doFilterInternalMethod, "CorsFilter should have doFilterInternal method")
    }
}
