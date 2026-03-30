package com.example.a4zadanie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.UUID

data class Product(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val sku: String,
    val quantity: String,
    val discount: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme(primary = Color(0xFFE50914))) {
                NikitiumLuxuryShop()
            }
        }
    }
}

@Composable
fun NikitiumLuxuryShop() {
    //поля для ввода
    var name by rememberSaveable { mutableStateOf("") }
    var sku by rememberSaveable { mutableStateOf("") }
    var quantity by rememberSaveable { mutableStateOf("") }
    var discount by rememberSaveable { mutableStateOf("") }

    //Сохранение состояния при повороте
    val productList = rememberSaveable(
        saver = listSaver(
            save = { list -> list.map { listOf(it.id, it.name, it.sku, it.quantity, it.discount) } },
            restore = { saved ->
                saved.map {
                    val item = it as List<String>
                    Product(id = item[0], name = item[1], sku = item[2], quantity = item[3], discount = item[4])
                }.toMutableStateList()
            }
        )
    ) { mutableStateListOf<Product>() }

    val bgGradient = Brush.verticalGradient(listOf(Color(0xFF0F0F0F), Color(0xFF1A1A1A)))
    val redGradient = Brush.horizontalGradient(listOf(Color(0xFF800000), Color(0xFFE50914)))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient)
            .statusBarsPadding()
    ) {
        //  немного своих названий и дополнений
        Text(
            text = "NIKITIUM",
            modifier = Modifier.padding(start = 20.dp, top = 20.dp),
            style = TextStyle(
                color = Color(0xFFE50914),
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 6.sp
            )
        )
        Text(
            text = "ПРЕМИАЛЬНЫЙ МАРКЕТПЛЕЙС",
            modifier = Modifier.padding(start = 22.dp, bottom = 10.dp),
            style = TextStyle(color = Color.Gray, fontSize = 10.sp, letterSpacing = 2.sp)
        )

        // Блок ввода
        Surface(
            modifier = Modifier.padding(16.dp),
            color = Color.White.copy(alpha = 0.05f),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    NikitiumInput(name, { name = it }, "Название", Modifier.weight(1f))
                    NikitiumInput(sku, { sku = it }, "Артикул", Modifier.weight(1f))
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    NikitiumInput(quantity, { quantity = it }, "Кол-во", Modifier.weight(1f), KeyboardType.Number)
                    NikitiumInput(discount, { discount = it }, "Скидка %", Modifier.weight(1f), KeyboardType.Number)
                }

                Spacer(modifier = Modifier.height(16.dp))

                //кнопка  новой карточки
                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            productList.add(0, Product(name = name, sku = sku, quantity = quantity, discount = discount))
                            name = ""; sku = ""; quantity = ""; discount = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize().background(redGradient),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                            Spacer(Modifier.width(8.dp))
                            Text("ДОБАВИТЬ ТОВАР", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
        // Разделитель
        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
            Divider(modifier = Modifier.fillMaxWidth(0.5f), color = Color.DarkGray, thickness = 1.dp)
        }

        // 4. Список
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(productList, key = { it.id }) { product ->
                //Карточка
                NikitiumCard(
                    product = product,
                    onDelete = { productList.remove(product) }
                )
            }
        }
    }
}

@Composable
fun NikitiumInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 12.sp) },
        modifier = modifier.padding(bottom = 8.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFE50914),
            unfocusedBorderColor = Color.DarkGray,
            focusedLabelColor = Color(0xFFE50914)
        )
    )
}

@Composable
fun NikitiumCard(product: Product, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1C)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(10.dp, 24.dp).background(Color(0xFFE50914), RoundedCornerShape(2.dp)))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = product.name.uppercase(),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    StatInfo("АРТИКУЛ", product.sku)
                    StatInfo("КОЛ-ВО", "${product.quantity} ШТ")

                    //скидка
                    Surface(
                        color = Color(0xFFE50914),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "-${product.discount}%",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            //удаление
            IconButton(
                onClick = onDelete,
                modifier = Modifier.align(Alignment.TopEnd).offset(x = 10.dp, y = (-10).dp)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Удалить", tint = Color.Gray, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun StatInfo(label: String, value: String) {
    Column {
        Text(text = label, color = Color.Gray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        Text(text = value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}