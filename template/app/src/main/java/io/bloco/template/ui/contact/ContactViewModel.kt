package io.bloco.template.ui.contact

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.bloco.template.data.repository.ContactsRepository
import io.bloco.template.domain.models.BaseResponse
import io.bloco.template.domain.models.Contact
import io.bloco.template.shared.Resource
import io.bloco.template.ui.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ContactViewModel
@Inject constructor(
    private val contactsRepository: ContactsRepository
) : BaseViewModel() {

    val changesInContact = MutableLiveData<Pair<Int, Contact>>()

    fun fetchContactList(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<Contact>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { contactsRepository }
        ).flow
    }

    fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = 10, enablePlaceholders = false)
    }

    fun starContact(contactId: Int, isStared: Boolean) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = if (isStared) contactsRepository.starContact(contactId)
                        .body() else contactsRepository.unstarContact(
                        contactId
                    ).body()
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}