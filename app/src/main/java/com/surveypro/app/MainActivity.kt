package com.surveypro.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class SurveyRecord(
    val id: String,
    val siteName: String,
    val inspector: String,
    val category: String,
    val score: Int,
    val notes: String,
    val gpsCoords: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SurveyProScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyProScreen() {
    var siteName by remember { mutableStateOf("") }
    var inspector by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Site Safety") }
    var score by remember { mutableStateOf(90f) }
    var notes by remember { mutableStateOf("") }
    var records by remember {
        mutableStateOf(
            listOf(
                SurveyRecord("SP-101", "North Sector Substation", "K. Higson", "Site Safety", 94, "Perimeter fencing verified, grounding intact.", "51.5074° N, 0.1278° W"),
                SurveyRecord("SP-102", "Meridian Ridge Plot B", "A. Vance", "Land Boundary", 88, "Corner markers 1-4 logged via RTK GPS.", "53.4808° N, 2.2426° W")
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SurveyPro Field Collector", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("New Field Entry", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = siteName,
                    onValueChange = { siteName = it },
                    label = { Text("Site / Location Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = inspector,
                    onValueChange = { inspector = it },
                    label = { Text("Surveyor Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Compliance / Quality Rating: ${score.toInt()}%")
                Slider(
                    value = score,
                    onValueChange = { score = it },
                    valueRange = 0f..100f
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Field Observations & Measurements") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        if (siteName.isNotBlank()) {
                            records = listOf(
                                SurveyRecord(
                                    id = "SP-${100 + records.size + 1}",
                                    siteName = siteName,
                                    inspector = inspector.ifBlank { "Field Inspector" },
                                    category = category,
                                    score = score.toInt(),
                                    notes = notes,
                                    gpsCoords = "51.5098° N, 0.1180° W"
                                )
                            ) + records
                            siteName = ""
                            notes = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Survey Record")
                }
            }

            item {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text("Logged Surveys (${records.size})", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }

            items(records) { item ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("${item.id} · ${item.siteName}", fontWeight = FontWeight.Bold)
                        Text("Surveyor: ${item.inspector} · Score: ${item.score}%", style = MaterialTheme.typography.bodySmall)
                        if (item.notes.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(item.notes, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
