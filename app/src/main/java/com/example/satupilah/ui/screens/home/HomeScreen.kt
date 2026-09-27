package com.example.satupilah.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.satupilah.Screen

enum class HomeTransactionType {
    DEPOSIT, WITHDRAWAL
}

data class HomeTransactionUi(
    val id: String,
    val type: HomeTransactionType,
    val date: String,
    val time: String,
    val amount: String,
    val userName: String,
    val itemsDetail: List<String> = emptyList()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var isFabExpanded by remember { mutableStateOf(false) }
    var showTimbangBottomSheet by remember { mutableStateOf(false) }
    var showPencairanBottomSheet by remember { mutableStateOf(false) }

    val rotationAngle by animateFloatAsState(
        targetValue = if (isFabExpanded) 45f else 0f,
        label = "FabRotation"
    )

    val sampleTransactions = listOf(
        HomeTransactionUi(
            id = "1",
            type = HomeTransactionType.DEPOSIT,
            date = "23 September",
            time = "08.46",
            amount = "Rp75,000",
            userName = "Jane Doe Abdulsalam",
            itemsDetail = listOf("Karton · 728g", "Botol Plastik · 210g", "Botol Kaca · 328g")
        ),
        HomeTransactionUi(
            id = "2",
            type = HomeTransactionType.DEPOSIT,
            date = "23 September",
            time = "08.46",
            amount = "Rp75,000",
            userName = "Jane Doe Abdulsalam",
            itemsDetail = listOf("Karton · 728g", "Botol Plastik · 210g")
        ),
        HomeTransactionUi(
            id = "3",
            type = HomeTransactionType.WITHDRAWAL,
            date = "23 September",
            time = "08.46",
            amount = "Rp120,000",
            userName = "Jane Doe Abdulsalam"
        )
    )

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AnimatedVisibility(
                    visible = isFabExpanded,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SmallExtendedFabItem(
                            text = "Mulai Timbang",
                            icon = Icons.Default.Recycling,
                            containerColor = MaterialTheme.colorScheme.primary,
                            onClick = {
                                isFabExpanded = false
                                showTimbangBottomSheet = true
                            }
                        )

                        SmallExtendedFabItem(
                            text = "Pencairan",
                            icon = Icons.Default.Payments,
                            containerColor = MaterialTheme.colorScheme.primary,
                            onClick = {
                                isFabExpanded = false
                                showPencairanBottomSheet = true
                            }
                        )
                    }
                }

                FloatingActionButton(
                    onClick = { isFabExpanded = !isFabExpanded },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Menu Aksi",
                        modifier = Modifier
                            .rotate(rotationAngle)
                            .size(28.dp)
                    )
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(top = 0.dp, bottom = 24.dp)
        ) {
            item {
                Text(
                    text = "Selamat Pagi",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Rabu, 23 September",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(32.dp))
                            Text(
                                text = "0,99kg",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Rp124.102",
                                fontSize = 28.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Recycling,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(128.dp)
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ShortcutNavigationCard(
                        title = "Kelola Petugas",
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate(Screen.Admin.route) }
                    )
                    ShortcutNavigationCard(
                        title = "Update Harga",
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate(Screen.Category.route) }
                    )
                }
            }

            item {
                Text(
                    text = "Riwayat",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top= 8.dp)
                )
            }

            items(sampleTransactions) { transaction ->
                HomeHistoryCardItem(item = transaction)
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }

        // Pop-up Bottom Sheet: Measure
        if (showTimbangBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showTimbangBottomSheet = false },
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            ) {
                TimbangBottomSheetContent(
                    onDismiss = { showTimbangBottomSheet = false }
                )
            }
        }

        // Pop-up Bottom Sheet: Withdrawal
        if (showPencairanBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showPencairanBottomSheet = false },
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            ) {
                PencairanBottomSheetContent(
                    onDismiss = { showPencairanBottomSheet = false }
                )
            }
        }
    }
}

// Measure Bottom Sheet
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimbangBottomSheetContent(onDismiss: () -> Unit) {
    var selectedNasabah by remember { mutableStateOf("") }
    var selectedKategori by remember { mutableStateOf("") }
    var beratInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Form Setor Sampah",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        OutlinedTextField(
            value = selectedNasabah,
            onValueChange = { selectedNasabah = it },
            label = { Text("Pilih Nasabah") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        OutlinedTextField(
            value = selectedKategori,
            onValueChange = { selectedKategori = it },
            label = { Text("Kategori Sampah") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        OutlinedTextField(
            value = beratInput,
            onValueChange = { beratInput = it },
            label = { Text("Berat Sampah (kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                // TODO: Proses Simpan Timbangan ke Database
                onDismiss()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(100)
        ) {
            Text("Proses Setor", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

// Withdrawal Bottom Sheet
@Composable
fun PencairanBottomSheetContent(onDismiss: () -> Unit) {
    var selectedNasabah by remember { mutableStateOf("") }
    var nominalInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Form Pencairan Saldo",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        OutlinedTextField(
            value = selectedNasabah,
            onValueChange = { selectedNasabah = it },
            label = { Text("Pilih Nasabah") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        OutlinedTextField(
            value = nominalInput,
            onValueChange = { nominalInput = it },
            label = { Text("Nominal Pencairan (Rp)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                // TODO: Proses Pencairan Saldo ke Database
                onDismiss()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(100)
        ) {
            Text("Cairkan Saldo", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun SmallExtendedFabItem(
    text: String,
    icon: ImageVector,
    containerColor: Color,
    onClick: () -> Unit
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        containerColor = containerColor,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        icon = { Icon(icon, contentDescription = null) },
        text = { Text(text, fontWeight = FontWeight.SemiBold) }
    )
}

@Composable
fun ShortcutNavigationCard(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun HomeHistoryCardItem(item: HomeTransactionUi) {
    val isDeposit = item.type == HomeTransactionType.DEPOSIT
    val cardBg = if (isDeposit) MaterialTheme.colorScheme.surfaceContainerLow else MaterialTheme.colorScheme.primaryContainer
    val iconVector: ImageVector = if (isDeposit) Icons.Default.Recycling else Icons.Default.Payments
    val titleText = if (isDeposit) "Setor Sampah" else "Pencairan Saldo"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = iconVector,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = titleText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${item.date} · ${item.time}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(50)
                        )
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = item.amount,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = item.userName,
                fontSize = 22.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (isDeposit && item.itemsDetail.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    item.itemsDetail.forEach { detail ->
                        Text(
                            text = detail,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen(navController = rememberNavController())
    }
}