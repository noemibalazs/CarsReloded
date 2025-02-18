package com.noemi.carsreloaded.usecase

import com.noemi.carsreloaded.model.local.Car
import com.noemi.carsreloaded.repository.CarRepository

class FilterByPlateNumberUseCase(private val carRepository: CarRepository) {

    suspend operator fun invoke(plateNumber: String): List<Car> = carRepository.filteredByPlateNumber(plateNumber)
}