package com.noemi.carsreloaded.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.noemi.carsreloaded.model.local.Car
import com.noemi.carsreloaded.screens.map.MapViewModel
import com.noemi.carsreloaded.usecase.GetCarsUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MapViewModelTest {

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val getCarsUseCase: GetCarsUseCase = mockk()
    private val carsObserver: Observer<List<Car>> = mockk()
    private val carsCaptor = mutableListOf<List<Car>>()

    private val car: Car = mockk()
    private val cars = listOf(car)

    private lateinit var viewModel: MapViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = MapViewModel(getCarsUseCase)

        carsCaptor.clear()
        coEvery { carsObserver.onChanged(capture(carsCaptor)) } just runs
        viewModel.cars.observeForever(carsObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test load cars returns saved cars list`() = runBlocking {
        coEvery { getCarsUseCase.invoke() } returns cars

        viewModel.loadCars()

        coVerify { getCarsUseCase.invoke() }
        coVerify { carsObserver.onChanged(cars) }
    }

    @Test
    fun `test load cars returns empty list`() = runBlocking {
        coEvery { getCarsUseCase.invoke() } returns emptyList()

        viewModel.loadCars()

        coVerify { getCarsUseCase.invoke() }
        coVerify { carsObserver.onChanged(emptyList()) }
    }
}