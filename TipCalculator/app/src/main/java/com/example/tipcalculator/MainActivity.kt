package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.fromColorLong
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tipcalculator.ui.theme.TipCalculatorTheme

val purple = Color(0xFF6911C6)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipCalculatorTheme {
                Scaffold(
                    topBar = {
                       TitleBar("Tip Calculator")
                    },
                    modifier = Modifier.fillMaxSize())
                { innerPadding ->
                    val tip = rememberTextFieldState(initialText = "")
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {
                        Spacer(modifier = Modifier.padding(10.dp))
                        Row() {
                            TipTextField(tip)
                            Button(
                                onClick = {
                                    tip.edit { replace(0, length, "") }
                                },
                                colors = buttonColors(
                                    containerColor = purple
                                ),
                            ) {
                                Text(text = "Reset")
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleBar(titleText:String, modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = titleText,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = purple
        ),
    )
}

@Composable
fun TipTextField(tip: TextFieldState, modifier: Modifier = Modifier) {
    OutlinedTextField(
        state = tip,
        placeholder = { Text(text = "Insert tip") },
        prefix = { Text(text = "$")},
        lineLimits = TextFieldLineLimits.SingleLine,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        inputTransformation = {
            // asCharSequence is the new text
            val newText = asCharSequence().toString()
            if (!newText.matches(Regex("""^\d*\.?\d*$"""))) {
                revertAllChanges() // Block the invalid character
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    TitleBar("Tip Calculator")
}