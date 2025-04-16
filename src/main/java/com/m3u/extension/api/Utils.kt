package com.m3u.extension.api

import java.lang.reflect.Parameter
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.WildcardType

fun Parameter.getRealParameterizedType(): Type {
    val type = (parameterizedType as ParameterizedType).actualTypeArguments[0]
    return when (type) {
        is WildcardType -> {
            type.lowerBounds.firstOrNull() ?: type.upperBounds.firstOrNull() ?: type
        }
        else -> type
    }
}