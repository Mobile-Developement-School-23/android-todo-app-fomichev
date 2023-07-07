package com.example.mytodoapp.di

import javax.inject.Scope

/**
 * Represents a Dagger scope annotation for application-wide dependencies.
 * This scope is used to define dependencies with application-level scope.
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope


/**
 * Represents a Dagger scope annotation for fragment-level dependencies.
 * This scope is used to define dependencies with fragment-level scope.
 *  */
@Scope
annotation class FragmentScope