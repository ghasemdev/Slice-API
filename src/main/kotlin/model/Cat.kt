package model

import kotlinx.serialization.Serializable

@Serializable
data class Cat(var id: String? = null, var name: String? = null, var age: Int? = null)