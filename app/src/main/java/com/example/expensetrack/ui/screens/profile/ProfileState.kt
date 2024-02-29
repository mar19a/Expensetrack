package com.example.expensetrack.ui.screens.profile

import com.example.expensetrack.data.model.User
import com.example.expensetrack.data.repository.UserRepository
import com.example.expensetrack.ui.domain.FormState
import com.example.expensetrack.ui.domain.MutableObservableState
import com.example.expensetrack.utils.createErrorState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ProfileState(
    private val repo: UserRepository,
    scope: CoroutineScope,
) : FormState<User>() {

    private val starting: String? = null

    val nameError = createErrorState()
    val name = MutableObservableState(
        initial = starting,
        update = { it: String?, _ -> it },
        output = { it ?: "" }
    )

    val jobDescriptionError = createErrorState()
    val jobDescription = MutableObservableState(
        initial = starting,
        update = { it: String?, _ -> it },
        output = { it ?: "" }
    )

    val departmentError = createErrorState()
    val department = MutableObservableState(
        initial = starting,
        update = { it: String?, _ -> it },
        output = { it ?: "" }
    )

    val locationError = createErrorState()
    val location = MutableObservableState(
        initial = starting,
        update = { it: String?, _ -> it },
        output = { it ?: "" }
    )

    val avatar = MutableObservableState(
        initial = starting,
        update = { it: String?, _ -> it },
        output = { it }
    )

    val showGallery = MutableObservableState(
        initial = false,
        update = { show: Boolean, _ -> show },
        output = { it }
    )

    init {
        scope.launch {
            repo.get().collect { user ->
                name.emit(user?.name)
                jobDescription.emit(user?.jobDescription)
                department.emit(user?.department)
                location.emit(user?.location)
                avatar.emit(user?.avatar)
            }
        }
    }

    override val errors = listOf(nameError, jobDescriptionError, departmentError, locationError)

    override suspend fun save(): User? {
        val errorMessage = "Please provide a %s"
        clearErrors()

        if (name.value.isNullOrBlank())
            nameError.emit(errorMessage.format("name"))
        if (jobDescription.value.isNullOrBlank())
            jobDescriptionError.emit(errorMessage.format("jobDescription"))
        if (department.value.isNullOrBlank())
            departmentError.emit(errorMessage.format("department"))
        if (location.value.isNullOrBlank())
            locationError.emit(errorMessage.format("location"))

        return if (hasError()) {
            null
        } else User(
            name = name.value!!,
            jobDescription = jobDescription.value!!,
            department = department.value!!,
            location = location.value!!,
            avatar = avatar.value,
        ).also { user ->
            repo.save(user)
        }
    }
}
