import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.makart.DrawingListItem
import com.example.makart.DrawingViewModel
import com.example.makart.Screen

@Composable
fun MainMenuScreen(navController: NavHostController, drawingViewModel: DrawingViewModel = viewModel()) {
    val drawingList by drawingViewModel.drawingList.collectAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { navController.navigate(Screen.DrawEditor.createRoute(0)) }) { // Navigate with default drawingId
            Text(text = "Draw")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            items(drawingList) { drawingEntity ->
                DrawingListItem(drawingEntity = drawingEntity, onClick = {
                    navController.navigate(Screen.DrawEditor.createRoute(drawingEntity.id))
                })
            }
        }
    }
}

