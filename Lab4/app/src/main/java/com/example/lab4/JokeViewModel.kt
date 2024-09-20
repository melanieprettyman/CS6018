import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab4.JokeApi
import com.example.lab4.JokeDao
import com.example.lab4.JokeDatabase
import com.example.lab4.JokeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JokeViewModel(application: Application) : AndroidViewModel(application) {

    //Init JokeDao to access the local Room database for jokes.
    private val jokeDao: JokeDao = JokeDatabase.getDatabase(application).jokeDao()

    val jokeList: Flow<List<JokeEntity>> = jokeDao.getAllJokes()

    private val _currentJoke = MutableStateFlow("Loading joke...")
    val currentJoke: StateFlow<String> = _currentJoke

    //Coroutine that makes a network request to fetch a random joke. The fetched joke is inserted
    // into the Room database. The MutableStateFlow _currentJoke is updated with the fetched joke,
    // and this triggers an update to the UI.
    fun fetchNewJoke() {
        viewModelScope.launch {
            try {
                // Replace with your API call to fetch the joke
                val jokeResponse = JokeApi.retrofitService.getRandomJoke()
                val joke = jokeResponse.value

                // Insert the joke into the database
                jokeDao.insertJoke(JokeEntity(joke = joke))

                // Update current joke
                _currentJoke.value = joke
            } catch (e: Exception) {
                _currentJoke.value = "Failed to load joke: ${e.message}"
            }
        }
    }
}
