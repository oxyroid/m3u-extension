package com.m3u.annotation

import java.lang.annotation.Inherited

@Inherited
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class InheritedOverride
