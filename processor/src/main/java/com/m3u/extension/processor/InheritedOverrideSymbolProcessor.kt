package com.m3u.extension.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.isAbstract
import com.google.devtools.ksp.isOpen
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.validate
import com.m3u.annotation.InheritedOverride
import com.squareup.kotlinpoet.ksp.toTypeName
import kotlin.reflect.KClass

class InheritedOverrideSymbolProcessor(private val logger: KSPLogger) : SymbolProcessor {

    private val qualifiedName = InheritedOverride::class.qualifiedName!!
    private val simpleName = InheritedOverride::class.simpleName!!

    @OptIn(KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val allAnnotated = resolver
            .getSymbolsWithAnnotation(qualifiedName)
            .filterIsInstance<KSFunctionDeclaration>()
            .toList()

        val deferred = allAnnotated.filterNot { it.validate() }
        val validBaseMethods = allAnnotated - deferred

        validBaseMethods.forEach { fn ->
            if (!fn.isOpen() && !fn.isAbstract) {
                logger.error(
                    "@$simpleName can only be annotated on open or abstract methods.",
                    fn
                )
            }
        }

        val baseMap = validBaseMethods.groupBy { it.parentDeclaration as KSClassDeclaration }

        if (baseMap.isEmpty()) return deferred

        val allClasses = resolver.getAllFiles()
            .flatMap { file -> file.declarations.filterIsInstance<KSClassDeclaration>() }

        baseMap.forEach { (baseCls, methods) ->
            val subclasses = allClasses.filter { cls ->
                cls.getAllSuperTypes().any { it.declaration == baseCls }
            }
            subclasses.forEach { implCls ->
                methods.forEach { baseFn ->
                    val baseFnName = baseFn.simpleName.asString()
                    implCls.getAllFunctions()
                        .filter { it.findOverridee() == baseFn }
                        .forEach { childFn ->
                            if ((implCls.isOpen() || implCls.isAbstract()) && (childFn.isAbstract || childFn.isOpen())) {
                                val annotations = baseFn.annotations.toList()
                                val missing = annotations.filter { it !in childFn.annotations }.map { it.annotationType.toTypeName() }
                                if (missing.isNotEmpty()) {
                                    logger.error(
                                        "Since this abstract or open method overrides \"$baseFnName\" marked with @InheritedOverride, " +
                                                "it should also inherit its all annotations. Missing annotations: $missing",
                                        childFn
                                    )
                                }
                            } else {
                                val annotations = baseFn.annotations.toMutableSet()
                                annotations.removeIf { it.isAnnEquals(InheritedOverride::class) }
                                val missing = annotations.filter { ann ->
                                    childFn.annotations.none { it.isAnnEquals(ann) }
                                }.map { it.annotationType.toTypeName() }
                                if (missing.isNotEmpty()) {
                                    logger.error(
                                        "Since this final method overrides \"$baseFnName\" marked with @InheritedOverride, " +
                                                "it should also inherit its other annotations. Missing annotations: $missing",
                                        childFn
                                    )
                                }
                            }
                        }
                }
            }
        }

        return deferred
    }



    private fun processImpl(
        resolver: Resolver,
        qualifiedName: String,
        simpleName: String
    ): List<KSAnnotated> {
        val allAnnotated = resolver
            .getSymbolsWithAnnotation(qualifiedName)
            .filterIsInstance<KSFunctionDeclaration>()
            .toList()

        val deferred = allAnnotated.filterNot { it.validate() }
        val validBaseMethods = allAnnotated - deferred

        validBaseMethods.forEach { fn ->
            if (!fn.isOpen() && !fn.isAbstract) {
                logger.error(
                    "@$simpleName can only be annotated on open or abstract methods.",
                    fn
                )
            }
        }

        val baseMap = validBaseMethods.groupBy { it.parentDeclaration as KSClassDeclaration }

        if (baseMap.isEmpty()) return deferred

        val allClasses = resolver.getAllFiles()
            .flatMap { file -> file.declarations.filterIsInstance<KSClassDeclaration>() }

        baseMap.forEach { (baseCls, methods) ->
            val subclasses = allClasses.filter { cls ->
                cls.getAllSuperTypes().any { it.declaration == baseCls }
            }
            subclasses.forEach { implCls ->
                methods.forEach { baseFn ->
                    val baseFnName = baseFn.simpleName.asString()
                    implCls.getAllFunctions()
                        .filter { it.findOverridee() == baseFn }
                        .forEach { childFn ->
                            if ((implCls.isOpen() || implCls.isAbstract()) && (childFn.isAbstract || childFn.isOpen())) {
                                val annotations = baseFn.annotations.toList()
                                val missing = annotations.filter { it !in childFn.annotations }.map { it.annotationType.toTypeName() }
                                if (missing.isNotEmpty()) {
                                    logger.error(
                                        "Since this abstract or open method overrides \"$baseFnName\" marked with @${simpleName}, " +
                                                "it should also inherit its all annotations. Missing annotations: $missing",
                                        childFn
                                    )
                                }
                            } else {
                                val annotations = baseFn.annotations.toMutableSet()
                                annotations.removeIf { it.isAnnEquals(InheritedOverride::class) }
                                val missing = annotations.filter { ann ->
                                    childFn.annotations.none { it.isAnnEquals(ann) }
                                }.map { it.annotationType.toTypeName() }
                                if (missing.isNotEmpty()) {
                                    logger.error(
                                        "Since this final method overrides \"$baseFnName\" marked with @$simpleName, " +
                                                "it should also inherit its other annotations. Missing annotations: $missing",
                                        childFn
                                    )
                                }
                            }
                        }
                }
            }
        }
        return deferred
    }
}

private fun <A : Any> KSAnnotation.isAnnEquals(clazz: KClass<A>): Boolean {
    val simpleName = clazz.simpleName
    val qualifiedName = clazz.qualifiedName
    return shortName.getShortName() == simpleName && annotationType.resolve().declaration
        .qualifiedName?.asString() == qualifiedName
}
private fun KSAnnotation.isAnnEquals(another: KSAnnotation): Boolean {
    return shortName.getShortName() == another.shortName.getShortName() && annotationType.resolve().declaration
        .qualifiedName?.asString() == another.annotationType.resolve().declaration.qualifiedName?.asString()
}