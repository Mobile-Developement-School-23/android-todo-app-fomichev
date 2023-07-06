package com.example.mytodoapp.di

import javax.inject.Scope

/**
 * This annotation represents a custom scope called ApplicationScope.
 * It is used to annotate components, modules, and dependencies that have application-wide scope.
 * The scope follows the single responsibility principle by focusing on the specific
 * task of defining the application scope.
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope