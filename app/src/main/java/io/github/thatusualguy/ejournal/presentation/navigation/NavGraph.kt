package io.github.thatusualguy.ejournal.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.github.thatusualguy.ejournal.presentation.ui.HomeScreen
import io.github.thatusualguy.ejournal.presentation.ui.SettingsScreen
import io.github.thatusualguy.ejournal.presentation.ui.TimetableScreen
import io.github.thatusualguy.ejournal.presentation.ui.login.LoginScreen
import io.github.thatusualguy.ejournal.presentation.ui.login.NoRoleScreen
import io.github.thatusualguy.ejournal.presentation.ui.login.SignUpScreen
import io.github.thatusualguy.ejournal.presentation.ui.profile.GroupProfileScreen
import io.github.thatusualguy.ejournal.presentation.ui.profile.StudentProfileScreen
import io.github.thatusualguy.ejournal.presentation.ui.profile.TeacherProfileScreen
import io.github.thatusualguy.ejournal.presentation.ui.search.SearchGroupScreen
import io.github.thatusualguy.ejournal.presentation.ui.search.SearchStudentScreen
import io.github.thatusualguy.ejournal.presentation.ui.search.SearchTeacherScreen
import io.github.thatusualguy.ejournal.presentation.ui.subjects.LessonsScreen
import io.github.thatusualguy.ejournal.presentation.ui.subjects.SubjectsScreen

@Composable
fun MyNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavRoute.Login.route
    ) {
        composable(route = NavRoute.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(route = NavRoute.Signup.route) {
            SignUpScreen(navController = navController)
        }
        composable(route = NavRoute.NoRole.route){
            NoRoleScreen(navController = navController)
        }
        composable(route = NavRoute.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(route = NavRoute.Settings.route) {
            SettingsScreen(navController = navController)
        }
        composable(route = NavRoute.Subjects.route) {
            SubjectsScreen(navController = navController)
        }
        composable(route = NavRoute.SearchStudents.route) {
            SearchStudentScreen(navController = navController)
        }
        composable(route = NavRoute.SearchGroups.route) {
            SearchGroupScreen(navController = navController)
        }
        composable(route = NavRoute.SearchTeachers.route) {
            SearchTeacherScreen(navController = navController)
        }
        composable(route = NavRoute.Profile.route) {
            StudentProfileScreen(navController = navController, null)
        }

        composable(
            route = NavRoute.Timetable.route
                    + "?${NavArg.GroupId.name}={${NavArg.GroupId.name}}"
                    + "?${NavArg.TeacherId.name}={${NavArg.TeacherId.name}}",
            arguments = listOf(
                navArgument(NavArg.GroupId.name) { type = NavType.IntType; defaultValue = -1 },
                navArgument(NavArg.TeacherId.name) { type = NavType.IntType; defaultValue = -1 }
            )
        ) { backStackEntry ->
            TimetableScreen(
                navController = navController,
                groupId = backStackEntry.arguments?.getInt(NavArg.GroupId.name),
                teacherId = backStackEntry.arguments?.getInt(NavArg.TeacherId.name),
            )
        }

        composable(route = NavRoute.SubjectMarks.route + "/${NavArg.SubjectId.name}={${NavArg.SubjectId.name}}",
            arguments = listOf(
                navArgument(NavArg.SubjectId.name) { type = NavType.IntType }
            )
        ) {
            val subjectId = it.arguments?.getInt(NavArg.SubjectId.name)
            if (subjectId != null) {
                LessonsScreen(navController = navController, subjectId = subjectId)
            }
        }

        composable(route = NavRoute.Teacher.route + "/${NavArg.TeacherId.name}={${NavArg.TeacherId.name}}",
            arguments = listOf(
                navArgument(NavArg.TeacherId.name) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            TeacherProfileScreen(
                navController = navController,
                teacherId = backStackEntry.arguments?.getInt(NavArg.TeacherId.name),
            )
        }
        composable(route = NavRoute.Student.route + "/${NavArg.StudentId.name}={${NavArg.StudentId.name}}",
            arguments = listOf(
                navArgument(NavArg.StudentId.name) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            StudentProfileScreen(
                navController = navController,
                studentId = backStackEntry.arguments?.getInt(NavArg.StudentId.name),
            )
        }
        composable(route = NavRoute.Group.route + "/${NavArg.GroupId.name}={${NavArg.GroupId.name}}",
            arguments = listOf(
                navArgument(NavArg.GroupId.name) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            GroupProfileScreen(
                navController = navController,
                groupId = backStackEntry.arguments?.getInt(NavArg.GroupId.name),
            )
        }
    }
}