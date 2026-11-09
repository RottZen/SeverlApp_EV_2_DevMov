package com.example.myapplication

import com.example.severlapp_cenamad.repository.Repository
import com.example.myapplication.viewmodel.HomeViewModel
import com.example.myapplication.viewmodel.isValidDemoEToken
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RepositoryTest {
    @Test
    fun blankSearchReturnsAllProjects() {
        assertEquals(Repository.proyectos, Repository.buscarProyectos("  "))
    }

    @Test
    fun searchMatchesProjectMetadataAndRelatedResearchFields() {
        assertEquals(listOf(Repository.proyectos[0], Repository.proyectos[2]), Repository.buscarProyectos(" 2024 "))
        assertEquals(listOf(Repository.proyectos[4]), Repository.buscarProyectos("ecoforest"))
        assertEquals(
            listOf(Repository.proyectos[2], Repository.proyectos[3]),
            Repository.buscarProyectos("estructural")
        )
        assertTrue(Repository.buscarProyectos("en ejecución").isNotEmpty())
    }

    @Test
    fun demoETokenMustMatchTheConfiguredCode() {
        assertTrue(isValidDemoEToken("TOKEN2026"))
        assertTrue(isValidDemoEToken("  TOKEN2026 "))
        assertFalse(isValidDemoEToken("wrong-token"))
        assertFalse(isValidDemoEToken(" "))
    }

    @Test
    fun homeViewModelOwnsSearchQueryAndResults() {
        val viewModel = HomeViewModel()

        viewModel.onQueryChanged("  estructural ")

        assertEquals("  estructural ", viewModel.uiState.query)
        assertEquals(listOf(Repository.lineas[1]), viewModel.uiState.lineas)
        assertEquals(listOf(Repository.proyectos[2], Repository.proyectos[3]), viewModel.uiState.proyectos)
    }
}
