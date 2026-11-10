
package com.homefit.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val DarkBg = Color(0xFF0B1220)
private val CardBg = Color(0xFF19263A)
private val Accent = Color(0xFF39D98A)
private val Muted = Color(0xFFA8B5C8)

data class Exercise(
    val name: String,
    val tamil: String,
    val sets: String
)

data class Workout(
    val day: String,
    val title: String,
    val tamil: String,
    val exercises: List<Exercise>
)

private val plans = listOf(
    Workout("MON", "Chest + Triceps", "மார்பு + கைப்பின்புறம்", listOf(
        Exercise("Wall Push-ups", "சுவர் புஷ்-அப்ஸ்", "2 × 12"),
        Exercise("Incline Push-ups", "சாய்வு புஷ்-அப்ஸ்", "2 × 8"),
        Exercise("Knee Push-ups", "முழங்கால் புஷ்-அப்ஸ்", "2 × 6"),
        Exercise("Triceps Push-ups", "ட்ரைசெப்ஸ் புஷ்-அப்ஸ்", "2 × 10")
    )),
    Workout("TUE", "Back + Biceps", "முதுகு + கைமுன்புறம்", listOf(
        Exercise("Superman Hold", "சூப்பர்மேன்", "2 × 10"),
        Exercise("Reverse Snow Angels", "ரிவர்ஸ் ஸ்னோ ஏஞ்சல்ஸ்", "2 × 10"),
        Exercise("Bird Dog", "பேர்ட் டாக்", "2 × 10"),
        Exercise("Self-resisted Curls", "பைசெப்ஸ் கர்ல்ஸ்", "2 × 10")
    )),
    Workout("WED", "Legs + Glutes", "கால்கள் + புட்டத் தசைகள்", listOf(
        Exercise("Chair Squats", "நாற்காலி ஸ்குவாட்ஸ்", "2 × 12"),
        Exercise("Reverse Lunges", "ரிவர்ஸ் லஞ்சஸ்", "2 × 8"),
        Exercise("Glute Bridges", "குளூட் பிரிட்ஜஸ்", "2 × 12"),
        Exercise("Calf Raises", "கால் முனை உயர்த்துதல்", "2 × 15")
    )),
    Workout("THU", "Shoulders + Abs", "தோள்கள் + வயிறு", listOf(
        Exercise("Arm Circles", "கை வட்டங்கள்", "2 × 20 sec"),
        Exercise("Dead Bug", "டெட் பக்", "2 × 10"),
        Exercise("Crunches", "க்ரஞ்சஸ்", "2 × 12"),
        Exercise("Knee Plank", "முழங்கால் பிளாங்க்", "2 × 20 sec")
    )),
    Workout("FRI", "Full Body", "முழு உடல்", listOf(
        Exercise("Bodyweight Squats", "ஸ்குவாட்ஸ்", "2 × 12"),
        Exercise("Wall Push-ups", "சுவர் புஷ்-அப்ஸ்", "2 × 12"),
        Exercise("Bird Dog", "பேர்ட் டாக்", "2 × 10"),
        Exercise("Glute Bridges", "குளூட் பிரிட்ஜஸ்", "2 × 12"),
        Exercise("March in Place", "ஒரே இடத்தில் நடை", "2 × 30 sec")
    ))
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeFitApp()
        }
    }
}

@Composable
fun HomeFitApp() {
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedDay by remember { mutableIntStateOf(0) }
    var completed by remember { mutableStateOf(setOf<String>()) }
    var weight by remember { mutableStateOf("") }
    var seconds by remember { mutableIntStateOf(60) }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Accent,
            background = DarkBg,
            surface = CardBg,
            onSurface = Color.White
        )
    ) {
        Scaffold(
            containerColor = DarkBg,
            bottomBar = {
                NavigationBar(containerColor = CardBg) {
                    val icons = listOf(
                        Icons.Default.Home,
                        Icons.Default.FitnessCenter,
                        Icons.Default.ShowChart,
                        Icons.Default.Person
                    )
                    val labels = listOf("Home", "Workout", "Progress", "Profile")

                    labels.forEachIndexed { index, label ->
                        NavigationBarItem(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            icon = {
                                Icon(icons[index], contentDescription = label)
                            },
                            label = { Text(label) }
                        )
                    }
                }
            }
        ) { padding ->
            when (selectedTab) {
                0 -> HomeScreen(padding, completed.size) {
                    selectedTab = 1
                }
                1 -> WorkoutScreen(
                    padding,
                    selectedDay,
                    { selectedDay = it },
                    completed,
                    { id, checked ->
                        completed = if (checked) {
                            completed + id
                        } else {
                            completed - id
                        }
                    },
                    seconds,
                    { seconds = it }
                )
                2 -> ProgressScreen(padding, completed.size, weight) {
                    weight = it
                }
                else -> ProfileScreen(padding)
            }
        }
    }
}

@Composable
fun HomeScreen(
    padding: PaddingValues,
    count: Int,
    start: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("HOMEFIT", color = Accent, fontSize = 30.sp, fontWeight = FontWeight.Black)
            Text("உங்கள் உடற்பயிற்சி பயணம்", color = Muted)
        }
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CardBg),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text("TODAY'S FITNESS", color = Muted)
                    Text("Train at Home", fontSize = 25.sp, fontWeight = FontWeight.Bold)
                    Text("வீட்டிலேயே உடற்பயிற்சி செய்யுங்கள்", color = Muted)
                    Spacer(Modifier.height(15.dp))
                    Button(
                        onClick = start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("START WORKOUT  →")
                    }
                }
            }
        }
        item {
            StatCard("Completed exercises", count.toString())
        }
        item {
            Text("WEEKLY PLAN", color = Accent, fontWeight = FontWeight.Bold)
            plans.forEachIndexed { index, plan ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)
                        .clickable { start() },
                    colors = CardDefaults.cardColors(containerColor = CardBg)
                ) {
                    Column(Modifier.padding(15.dp)) {
                        Text(plan.day, color = Accent)
                        Text(plan.title, fontWeight = FontWeight.Bold)
                        Text(plan.tamil, color = Muted)
                    }
                }
            }
        }
    }
}

@Composable
fun WorkoutScreen(
    padding: PaddingValues,
    selected: Int,
    select: (Int) -> Unit,
    completed: Set<String>,
    toggle: (String, Boolean) -> Unit,
    seconds: Int,
    setSeconds: (Int) -> Unit
) {
    val plan = plans[selected]

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("MY WORKOUT", fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text("உங்கள் பயிற்சி திட்டம்", color = Muted)
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                plans.forEachIndexed { index, item ->
                    FilterChip(
                        selected = selected == index,
                        onClick = { select(index) },
                        label = { Text(item.day) }
                    )
                }
            }
        }
        item {
            Text(plan.title, fontSize = 23.sp, fontWeight = FontWeight.Bold)
            Text(plan.tamil, color = Muted)
        }
        items(plan.exercises.size) { index ->
            val exercise = plan.exercises[index]
            val id = plan.day + exercise.name
            Card(colors = CardDefaults.cardColors(containerColor = CardBg)) {
                Row(
                    Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = completed.contains(id),
                        onCheckedChange = { toggle(id, it) }
                    )
                    Column(Modifier.weight(1f)) {
                        Text(exercise.name, fontWeight = FontWeight.Bold)
                        Text(exercise.tamil, color = Muted)
                        Text(exercise.sets, color = Accent)
                    }
                }
            }
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = CardBg)) {
                Column(Modifier.padding(16.dp)) {
                    Text("REST TIMER", fontWeight = FontWeight.Bold)
                    Text("$seconds seconds", color = Accent, fontSize = 28.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = {
                            setSeconds((seconds - 15).coerceAtLeast(0))
                        }) { Text("-15") }
                        Button(onClick = { setSeconds(60) }) {
                            Text("Reset")
                        }
                        OutlinedButton(onClick = {
                            setSeconds(seconds + 15)
                        }) { Text("+15") }
                    }
                    Text("Timer countdown will be activated in the next update.", color = Muted)
                }
            }
        }
    }
}

@Composable
fun ProgressScreen(
    padding: PaddingValues,
    count: Int,
    weight: String,
    onWeight: (String) -> Unit
) {
    LazyColumn(
        Modifier.fillMaxSize().padding(padding).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("MY PROGRESS", fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text("எனது முன்னேற்றம்", color = Muted)
        }
        item {
            StatCard("Completed exercises", count.toString())
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = CardBg)) {
                Column(Modifier.padding(16.dp)) {
                    Text("WEIGHT TRACKING", fontWeight = FontWeight.Bold)
                    Text("உடல் எடை பதிவு", color = Muted)
                    OutlinedTextField(
                        value = weight,
                        onValueChange = onWeight,
                        label = { Text("Weight in kg / கிலோ") },
                        singleLine = true
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(padding: PaddingValues) {
    Column(
        Modifier.fillMaxSize().padding(padding).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Text("PROFILE", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text("சுயவிவரம்", color = Muted)
        StatCard("HomeFit", "Local fitness journey")
        Text("Workout reminders and backup options will be added in the next versions.", color = Muted)
    }
}

@Composable
fun StatCard(title: String, value: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBg),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(18.dp)) {
            Text(title, color = Muted)
            Text(value, color = Accent, fontSize = 27.sp, fontWeight = FontWeight.Bold)
        }
    }
}
