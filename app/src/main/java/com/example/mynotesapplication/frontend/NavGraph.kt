package com.example.mynotesapplication.frontend

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val NOTES_LIST = "notes_list"
    const val CREATE_NOTE = "create_note"
}

@Composable
fun NoteNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.NOTES_LIST) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(Routes.REGISTER) }
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = { navController.popBackStack() },
                onBackToLoginClick = { navController.popBackStack() }
            )
        }
        composable(Routes.NOTES_LIST) {
            val notesViewModel: NotesViewModel = viewModel(factory = NotesViewModel.Factory)
            NotesListScreen(
                onAddNoteClick = { navController.navigate(Routes.CREATE_NOTE) },
                notesViewModel = notesViewModel
            )
        }
        composable(Routes.CREATE_NOTE) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Routes.NOTES_LIST)
            }
            val notesViewModel: NotesViewModel = viewModel(parentEntry, factory = NotesViewModel.Factory)
            CreateNoteScreen(
                onNoteSaved = { navController.popBackStack() },
                notesViewModel = notesViewModel
            )
        }
    }
}
