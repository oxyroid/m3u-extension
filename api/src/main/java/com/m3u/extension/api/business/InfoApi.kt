package com.m3u.extension.api.business

import com.m3u.annotation.InheritedOverride
import com.m3u.extension.api.Method
import com.m3u.extension.api.Module
import com.m3u.extension.api.model.GetAppInfoResponse
import com.m3u.extension.api.model.GetMethodsRequest
import com.m3u.extension.api.model.GetMethodsResponse
import com.m3u.extension.api.model.GetModulesResponse

@Module("info")
interface InfoApi {
    @Method("getAppInfo")
    @InheritedOverride
    suspend fun getAppInfo(): GetAppInfoResponse

    @Method("getModules")
    @InheritedOverride
    suspend fun getModules(): GetModulesResponse

    @Method("getMethods")
    @InheritedOverride
    suspend fun getMethods(req: GetMethodsRequest): GetMethodsResponse
}
