package com.openclassrooms.realestatemanager.ui.estate.upsert

import android.content.res.Resources
import androidx.core.text.isDigitsOnly
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.enums.PropertyType
import com.openclassrooms.realestatemanager.data.models.RealEstateAgent

class UpsertEstateValidation {
    companion object {
        fun validateType(resources: Resources, type: PropertyType?): ValidationResult {
            return if (type != null) {
                ValidationResult.Valid
            } else {
                ValidationResult.Invalid(resources.getString(R.string.form_error_not_empty))
            }
        }

        fun validatePrice(resources: Resources, price: String): ValidationResult {
            return if (price.isEmpty()) {
                ValidationResult.Invalid(resources.getString(R.string.form_error_not_empty))
            } else if (!price.isDigitsOnly()) {
                ValidationResult.Invalid(resources.getString(R.string.form_error_not_number))
            } else if (price.toLong() <= 0) {
                ValidationResult.Invalid(resources.getString(R.string.form_error_superior_to, 0))
            } else {
                ValidationResult.Valid
            }
        }

        fun validateSurface(resources: Resources, surface: String): ValidationResult {
            return if (surface.isEmpty()) {
                ValidationResult.Invalid(resources.getString(R.string.form_error_not_empty))
            } else if (!surface.isDigitsOnly()) {
                ValidationResult.Invalid(resources.getString(R.string.form_error_not_number))
            } else if (surface.toLong() <= 0) {
                ValidationResult.Invalid(resources.getString(R.string.form_error_superior_to, 0))
            } else {
                ValidationResult.Valid
            }
        }

        fun validateNbOfBathroom(resources: Resources, nbOfBathroom: String): ValidationResult {
            return if (nbOfBathroom.isEmpty()) {
                ValidationResult.Invalid(resources.getString(R.string.form_error_not_empty))
            } else if (!nbOfBathroom.isDigitsOnly()) {
                ValidationResult.Invalid(resources.getString(R.string.form_error_not_number))
            } else if (nbOfBathroom.toInt() < 0) {
                ValidationResult.Invalid(
                    resources.getString(
                        R.string.form_error_superior_or_equal_to,
                        0
                    )
                )
            } else {
                ValidationResult.Valid
            }
        }

        fun validateNbOfBedroom(resources: Resources, nbOfBedroom: String): ValidationResult {
            return if (nbOfBedroom.isEmpty()) {
                ValidationResult.Invalid(resources.getString(R.string.form_error_not_empty))
            } else if (!nbOfBedroom.isDigitsOnly()) {
                ValidationResult.Invalid(resources.getString(R.string.form_error_not_number))
            } else if (nbOfBedroom.toInt() < 0) {
                ValidationResult.Invalid(
                    resources.getString(R.string.form_error_superior_or_equal_to, 0)
                )
            } else {
                ValidationResult.Valid
            }
        }

        fun validateAddress(resources: Resources, address: String): ValidationResult {
            return if (address.isEmpty()) {
                ValidationResult.Invalid(resources.getString(R.string.form_error_not_empty))
            } else {
                ValidationResult.Valid
            }
        }

        fun validateZipcode(resources: Resources, zipcode: String): ValidationResult {
            return if (zipcode.isEmpty()) {
                ValidationResult.Invalid(resources.getString(R.string.form_error_not_empty))
            } else {
                ValidationResult.Valid
            }
        }

        fun validateCity(resources: Resources, city: String): ValidationResult {
            return if (city.isEmpty()) {
                ValidationResult.Invalid(resources.getString(R.string.form_error_not_empty))
            } else {
                ValidationResult.Valid
            }
        }

        fun validateCountry(resources: Resources, country: String): ValidationResult {
            return if (country.isEmpty()) {
                ValidationResult.Invalid(resources.getString(R.string.form_error_not_empty))
            } else {
                ValidationResult.Valid
            }
        }

        fun validateAgent(resources: Resources, agent: RealEstateAgent?): ValidationResult {
            return if (agent != null) {
                ValidationResult.Valid
            } else {
                ValidationResult.Invalid(resources.getString(R.string.form_error_not_empty))
            }
        }
    }

    sealed interface ValidationResult {

        val message: String
            get() = ""

        data object Valid : ValidationResult
        data class Invalid(override val message: String) : ValidationResult
    }
}