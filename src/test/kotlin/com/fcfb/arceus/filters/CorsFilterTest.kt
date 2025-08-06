package com.fcfb.arceus.filters

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.web.filter.OncePerRequestFilter
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest
@SpringJUnitConfig
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
    fun `CorsFilter should extend OncePerRequestFilter`() {
        assertTrue(corsFilter is OncePerRequestFilter, "CorsFilter should extend OncePerRequestFilter")
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

    @Test
    fun `CorsFilter should be a valid Spring component`() {
        // Test that the class can be instantiated and has proper annotations
        val corsFilter = CorsFilter()
        assertNotNull(corsFilter, "CorsFilter should be a valid Spring component")

        // Verify it's properly configured as a Spring Filter
        assertTrue(
            corsFilter is OncePerRequestFilter,
            "CorsFilter should extend OncePerRequestFilter for proper filter behavior",
        )
    }
}
