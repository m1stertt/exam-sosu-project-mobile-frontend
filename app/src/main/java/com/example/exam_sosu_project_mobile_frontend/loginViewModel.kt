package com.example.exam_sosu_project_mobile_frontend

import androidx.databinding.Bindable
import androidx.lifecycle.ViewModel
import com.example.exam_sosu_project_mobile_frontend.entities.Login

class LoginViewModel : ViewModel() {
    var data: Login=Login("","")

    @Bindable
    fun getLogin(): Login {
        return data
    }

    fun setLogin(value: Login) {
        // Avoids infinite loops.
        if (data.username != value.username&&data.role!=value.role) {
            data = value
        }
    }
}
