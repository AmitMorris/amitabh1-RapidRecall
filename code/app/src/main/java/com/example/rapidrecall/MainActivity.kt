package com.example.rapidrecall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rapidrecall.ui.theme.RapidRecallTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RapidRecallTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello ${name}!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RapidRecallTheme {
        Greeting("Android")
    }
}

@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf("INPUT") }
    var currentRound by remember { mutableStateOf<Round?>(null) }
    val summary by remember {mutableStateOf(Summary())}

    if (currentScreen == "INPUT") {
        LengthDeciderScreen(
            onStartGame = { length ->
                currentRound = Round(length)
                currentScreen = "SHOW_SEQUENCE"
            },
            onShowSummary = {
                currentScreen = "ROUND_LIST"
            },
            onShowGameSummary = {
                currentScreen = "GAME_SUMMARY"
            }
        )
    } else if (currentScreen == "SHOW_SEQUENCE") {
        currentRound?.let { round ->
            ShowSequenceScreen(
                sequenceString = round.getSequence().getSequence(),
                onSequenceFinished = {
                    currentScreen = "GUESS_SCREEN"
                }
            )
        }
    } else if (currentScreen == "GUESS_SCREEN") {
        currentRound?.let { round ->
            GuessNumberScreen(
                onGuessSubmitted = { userGuess ->
                    round.setGuess(userGuess)
                    summary.addRound(round)
                    currentScreen = "RESULT"
                }
            )
        }
    } else if (currentScreen == "RESULT") {
        currentRound?.let { round ->
            ResultScreen(
                round = round,
                onPlayAgain = {
                    currentScreen = "INPUT"
                },
                onShowSummary = {
                    currentScreen = "ROUND_LIST"
                },
                onShowGameSummary = {
                    currentScreen = "GAME_SUMMARY"
                }
            )
        }
    } else if (currentScreen == "ROUND_LIST") {
        ShowRounds(
            summary,
            onBackToGame = {
                currentScreen = "INPUT"
            },
            onShowGameSummary = {
                currentScreen = "GAME_SUMMARY"
            }
        )
    } else if (currentScreen == "GAME_SUMMARY") {
        GameSummary(
            summary,
            onBackToGame = {
                currentScreen = "INPUT"
            },
            onShowSummary = {
                currentScreen = "ROUND_LIST"
            }
        )
    }
}

@Composable
fun LengthDeciderScreen(
    onStartGame: (Int) -> Unit,
    onShowSummary: () -> Unit,
    onShowGameSummary: () -> Unit
) {
    var lengthInput by remember { mutableStateOf("") }
    var inputtedSomething by remember {mutableStateOf(false)}
    if (lengthInput == "") {
        inputtedSomething = false
    } else {
        inputtedSomething = true
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = "Rapid Recall",
            fontSize = 48.sp
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = lengthInput,
                onValueChange = { lengthInput = it.filter { char -> char.isDigit() } },
                label = { Text("Enter Sequence Length (1-10)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.width(200.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            if (inputtedSomething) {
                Button(
                    onClick = {
                        val length = lengthInput.toIntOrNull() ?: 1
                        onStartGame(length)
                    }
                ) {
                    Text("Start")
                }
            }
        }

        Row(modifier = Modifier.padding(16.dp)) {
            Button(onClick = onShowSummary) {
                Text("Round List")
            }
            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = onShowGameSummary) {
                Text("Game Summary")
            }
        }
    }
}

@Composable
fun ShowSequenceScreen(sequenceString: String, onSequenceFinished: () -> Unit) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var isVisible by remember {mutableStateOf(true)}

    LaunchedEffect(sequenceString) {
        for (i in sequenceString.indices) {
            currentIndex = i
            isVisible = true
            delay(850) // Display the digit for 850ms

            isVisible = false
            delay(150)
        }
        onSequenceFinished()
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Memorize the sequence!")
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (isVisible) sequenceString.getOrNull(currentIndex)?.toString() ?: "" else "",
            fontSize = 72.sp,

        )
    }
}

@Composable
fun GuessNumberScreen(onGuessSubmitted: (Long) -> Unit) {
    var guessInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("What was the sequence?")
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = guessInput,
            onValueChange = { guessInput = it.filter { char -> char.isDigit() } },
            label = { Text("Enter your guess") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val guess = guessInput.toLongOrNull() ?: 0L
            onGuessSubmitted(guess)
        }) {
            Text("Submit Guess")
        }
    }
}

@Composable
fun ResultScreen(
    round: Round,
    onPlayAgain: ()-> Unit,
    onShowSummary: ()->Unit,
    onShowGameSummary: ()->Unit
) {
    val isCorrect = round.compare()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isCorrect) "Correct!" else "Wrong!",
            fontSize = 32.sp,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Actual sequence: ${round.getSequence().getSequence()}")
        Text("Your guess: ${round.getGuess()}")

        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onPlayAgain) {
            Text("Play Again")
        }
        Row(modifier = Modifier.padding(16.dp)) {
            Button(onClick = onShowSummary) {
                Text("Round List")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onShowGameSummary) {
                Text("Game Summary")
            }
        }

    }
}

@Composable
fun RoundRow(round: Round) {
    Text(
        text = round.toString(),
        fontSize = 10.sp,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp,
            vertical = 14.dp)
    )
}

@Composable
fun ShowRounds(
    summary: Summary,
    onBackToGame:()->Unit,
    onShowGameSummary: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(top = 32.dp)) {
        Row(modifier = Modifier.padding(16.dp)) {
            Button(onClick = onBackToGame) {
                Text("Back to Game")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onShowGameSummary) {
                Text("Game Summary")
            }
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(summary.rounds) { round ->
                RoundRow(round = round)
            }
        }
    }
}

@Composable
fun GameSummary(
    summary: Summary,
    onBackToGame: () -> Unit,
    onShowSummary: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(top = 32.dp)) {
        Row(modifier = Modifier.padding(16.dp)) {
            Button(onClick = onBackToGame) {
                Text("Back to Game")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onShowSummary) {
                Text("Round List")
            }
        }
        Text(
            "Total Attempts: ${summary.totalAttempts()}",
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            "Successful Attempts: ${summary.successfulAttempts()}",
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            "Accuracy: ${summary.accuracyPercentage()}",
            fontSize = 24.sp
        )
    }
}