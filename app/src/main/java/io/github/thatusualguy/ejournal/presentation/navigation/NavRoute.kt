package io.github.thatusualguy.ejournal.presentation.navigation

sealed class NavArg(val name: String) {
    object SubjectId : NavArg("subjectId")
    object GroupId : NavArg("groupId")
    object TeacherId : NavArg("teacherId")
    object StudentId : NavArg("studentId")
}

sealed class NavRoute(val route: String) {
    object Login : NavRoute("login")
    object Signup : NavRoute("register")
    object NoRole : NavRoute("no_role")

    object Home : NavRoute("home")
    object Settings : NavRoute("settings")
    object Profile : NavRoute("profile")

    object Subjects : NavRoute("subjects")
    object SubjectMarks : NavRoute("subject_marks")

    object Timetable : NavRoute("timetable")
    object Student : NavRoute("student")
    object Group : NavRoute("group")
    object Teacher : NavRoute("teacher")

    object SearchStudents : NavRoute("search_student")
    object SearchGroups : NavRoute("search_group")
    object SearchTeachers : NavRoute("search_teacher")
}
