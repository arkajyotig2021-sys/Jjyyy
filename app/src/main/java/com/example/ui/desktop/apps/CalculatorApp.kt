package com.example.ui.desktop.apps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalculatorApp() {
    var display by remember { mutableStateOf("0") }
    var firstOperand by remember { mutableStateOf<Double?>(null) }
    var pendingOperator by remember { mutableStateOf<String?>(null) }
    var resetNext by remember { mutableStateOf(false) }

    fun onNumber(digit: String) {
        if (display == "0" || resetNext) {
            display = digit
            resetNext = false
        } else {
            display += digit
        }
    }

    fun onOperator(op: String) {
        firstOperand = display.toDoubleOrNull()
        pendingOperator = op
        resetNext = true
    }

    fun onCalculate() {
        val second = display.toDoubleOrNull() ?: 0.0
        val first = firstOperand ?: return
        val res = when (pendingOperator) {
            "+" -> first + second
            "-" -> first - second
            "×" -> first * second
            "÷" -> if (second != 0.0) first / second else 0.0
            else -> second
        }
        display = if (res % 1.0 == 0.0) res.toLong().toString() else res.toString()
        firstOperand = null
        pendingOperator = null
        resetNext = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Display
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFF1E293B)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = display,
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End
                )
            }
        }

        val buttons = listOf(
            listOf("C", "±", "%", "÷"),
            listOf("7", "8", "9", "×"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf("0", ".", "=")
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            buttons.forEach { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.forEach { btn ->
                        val isOp = btn in listOf("÷", "×", "-", "+", "=")
                        val isClear = btn == "C"
                        Button(
                            onClick = {
                                when {
                                    isClear -> {
                                        display = "0"
                                        firstOperand = null
                                        pendingOperator = null
                                    }
                                    btn == "=" -> onCalculate()
                                    isOp -> onOperator(btn)
                                    else -> onNumber(btn)
                                }
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = when {
                                    btn == "=" -> MaterialTheme.colorScheme.primary
                                    isOp -> Color(0xFF334155)
                                    else -> Color(0xFF1E293B)
                                }
                            ),
                            modifier = Modifier
                                .weight(if (btn == "0") 2f else 1f)
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = btn,
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
