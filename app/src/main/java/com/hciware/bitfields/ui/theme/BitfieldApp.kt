package com.hciware.bitfields.ui.theme

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hciware.bitfields.detail.DetailScreen
import com.hciware.bitfields.list.ListScreen
import com.hciware.bitfields.model.BitFieldsViewModel

@Composable
fun BitfieldApp() {
    val navController = rememberNavController()
    BitfieldNavHost(
        navController = navController
    )
}

const val ListRoute: String = "List"
const val DetailRoute: String = "Detail"

@Composable
fun BitfieldNavHost(navController: NavHostController) {

    val bitFieldsViewModel: BitFieldsViewModel = viewModel()
    NavHost(navController  = navController, startDestination = ListRoute) {
        composable(route = ListRoute) {
            ListScreen(
                selectBitField = {
                    bitFieldsViewModel.selectBitField(it)
                    navController.navigate(DetailRoute)
                },
                deleteBitField = { bitFieldsViewModel.delete(it)},
                bitFieldsViewModel = bitFieldsViewModel)
        }
        composable(route = DetailRoute) {
            bitFieldsViewModel.selectedBitField?.let { selectedBitfield ->
                selectedBitfield.description.name.let {
                    DetailScreen(
                        name = it.value,
                        {name -> selectedBitfield.description.name.value = name},
                        fields = selectedBitfield.sections,
                        bitFieldsViewModel.overallValueMode,
                        { mode -> bitFieldsViewModel.overallValueMode = mode },
                        bitFieldsViewModel.fieldsValueMode,
                        { mode -> bitFieldsViewModel.fieldsValueMode = mode },
                        selectedBitfield,
                        bitFieldsViewModel.editMode,
                        { editMode -> bitFieldsViewModel.editMode = editMode}
                    )
                }
            }
        }
    }
}
