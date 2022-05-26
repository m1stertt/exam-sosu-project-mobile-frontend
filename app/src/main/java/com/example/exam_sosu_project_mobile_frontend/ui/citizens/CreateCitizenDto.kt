package com.example.exam_sosu_project_mobile_frontend.ui.citizens

data class CreateCitizenDto(
    val firstName: String,
    val lastName: String,
    val birthday: String,
    val civilStatus: String,
    val address: CitizenAddressDto,
)
