package com.example.exam_sosu_project_mobile_frontend.entities

data class DawaAutoCompleteAddress(val tekst: String,val adresse:DawaAddress){
    override fun toString(): String = tekst
}
