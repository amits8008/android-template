package io.bloco.template.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dagger.hilt.android.scopes.ActivityRetainedScoped
import io.bloco.template.data.api.ApiHelper
import io.bloco.template.domain.models.Contact
import io.bloco.template.domain.models.BaseResponse
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class ContactsRepository @Inject constructor(private val apiHelper: ApiHelper) :
    PagingSource<Int, Contact>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Contact> {
        val pageNumber = params.key ?: 1
        try {
            val response = apiHelper.getContactList(pageNumber)
            val pagedResponse: BaseResponse<List<Contact>> =
                response.body() as BaseResponse<List<Contact>>

            var nextPageNumber: Int? = null

            val data = pagedResponse.content

            if (data?.size ?: -1 > 0) {
                nextPageNumber = pageNumber + 1
            }

            return PagingSource.LoadResult.Page(
                data = data.orEmpty(),
                prevKey = null,
                nextKey = nextPageNumber
            )
        } catch (exception: IOException) {
            return PagingSource.LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return PagingSource.LoadResult.Error(exception)
        } catch (e: Exception) {
            return PagingSource.LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Contact>): Int? {
        return state.anchorPosition?.let { state.closestItemToPosition(it)?.id }
    }

    suspend fun unstarContact(contactId: Int) = apiHelper.unstarContact(contactId)
    suspend fun starContact(contactId: Int) = apiHelper.starContact(contactId)
}