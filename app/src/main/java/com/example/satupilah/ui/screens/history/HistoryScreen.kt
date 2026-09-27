package com.example.satupilah.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class TransactionType {
    DEPOSIT, WITHDRAWAL
}

data class TransactionItemUi(
    val id: String,
    val type: TransactionType,
    val date: String,
    val time: String,
    val amount: String,
    val userName: String,
    val itemsDetail: List<String> = emptyList()
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HistoryScreen() {
    var selectedFilter by remember { mutableStateOf("Semua") }

    val sampleTransactions = listOf(
        TransactionItemUi(
            id = "1",
            type = TransactionType.DEPOSIT,
            date = "23 September",
            time = "08.46",
            amount = "Rp75,000",
            userName = "Jane Doe Abdulsalam",
            itemsDetail = listOf("Karton · 728g", "Botol Plastik · 210g", "Botol Kaca · 328g")
        ),
        TransactionItemUi(
            id = "2",
            type = TransactionType.DEPOSIT,
            date = "23 September",
            time = "08.46",
            amount = "Rp75,000",
            userName = "Jane Doe Abdulsalam",
            itemsDetail = listOf("Karton · 728g", "Botol Plastik · 210g", "Botol Kaca · 328g")
        ),
        TransactionItemUi(
            id = "3",
            type = TransactionType.WITHDRAWAL,
            date = "23 September",
            time = "08.46",
            amount = "Rp120.000",
            userName = "Jane Doe Abdulsalam"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Riwayat Transaksi",
            fontSize = 22.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 19.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            FilterChipItem(
                label = "Semua",
                isSelected = selectedFilter == "Semua",
                onClick = { selectedFilter = "Semua" }
            )
            FilterChipItem(
                label = "Setor Sampah",
                isSelected = selectedFilter == "Setor Sampah",
                onClick = { selectedFilter = "Setor Sampah" }
            )
            FilterChipItem(
                label = "Pencairan Saldo",
                isSelected = selectedFilter == "Pencairan Saldo",
                onClick = { selectedFilter = "Pencairan Saldo" }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Hari Ini",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(sampleTransactions) { item ->
                HistoryCardItem(item = item)
            }
        }
    }
}

@Composable
fun FilterChipItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface,
        border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline) else null,
        modifier = Modifier.height(38.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun HistoryCardItem(item: TransactionItemUi) {
    val isDeposit = item.type == TransactionType.DEPOSIT
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
