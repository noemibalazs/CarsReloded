package com.noemi.carsreloaded.screens.battery

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noemi.carsreloaded.model.local.Car
import com.noemi.carsreloaded.usecase.FilterByBatteryPercentageUseCase
import kotlinx.coroutines.launch

class BatteryViewModel(
    private val filterByBatteryPercentageUseCase: FilterByBatteryPercentageUseCase
) : ViewModel() {

    private val _cars = MutableLiveData<List<Car>>()
    val cars: LiveData<List<Car>> = _cars

    private val _hideKeyboard = MutableLiveData<Boolean>()
    val hideKeyBoard: LiveData<Boolean> = _hideKeyboard

    val batteryPercentage = ObservableField("")

    fun onFilterByPercentageClicked() {
        _hideKeyboard.value = true

        viewModelScope.launch {
            val percentage = batteryPercentage.get()?.toIntOrNull()
            val cars = filterByBatteryPercentageUseCase.invoke(percentage ?: 0)
            _cars.value = cars
            batteryPercentage.set("")
        }
    }

}