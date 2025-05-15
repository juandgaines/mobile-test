package com.juandgaines.seedqrvalidator.core.data.network

import com.juandgaines.seedqrvalidator.core.domain.utils.DataError
import com.juandgaines.seedqrvalidator.core.domain.utils.DataError.Network
import com.juandgaines.seedqrvalidator.core.domain.utils.Result
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

inline fun <reified T> safeCall (execute: () -> Response<T>): Result<T, DataError.Network> {
    val response = try {
        execute()
    } catch (e: UnknownHostException) {
        e.printStackTrace()
        return Result.Error(Network.NO_INTERNET)
    }catch (e: SerializationException){
        e.printStackTrace()
        return Result.Error(Network.SERIALIZATION)
    }
    catch (e: SocketTimeoutException){
        e.printStackTrace()
        return Result.Error(Network.REQUEST_TIMEOUT)
    }
    catch (e: Exception){
        if (e is CancellationException) throw e
        e.printStackTrace()
        return Result.Error(Network.UNKNOWN)
    }
    return responseToResult<T>(response)
}


inline fun <reified T> responseToResult(response: Response<T>): Result<T, Network> {
    return when (response.code()){
        in 200..299 -> {
            Result.Success(response.body() as T)
        }
        in 500..599 -> Result.Error(Network.SERVER_ERROR)
        else -> Result.Error(Network.UNKNOWN)
    }
}