package com.example.satupilah.ui.screens.home

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.satupilah.Screen
import com.example.satupilah.data.model.AdminUi
import com.example.satupilah.data.model.UserUiModel
import com.example.satupilah.data.model.WasteCategoryUi
import com.example.satupilah.ui.screens.history.HistoryCardItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current
    var isFabExpanded by remember { mutableStateOf(false) }
    var showTimbangBottomSheet by remember { mutableStateOf(false) }
    var showPencairanBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadDashboardData()
    }

    LaunchedEffect(showTimbangBottomSheet, showPencairanBottomSheet) {
        if (showTimbangBottomSheet || showPencairanBottomSheet) {
            viewModel.loadDashboardData()
        }
    }

    val rotationAngle by animateFloatAsState(
        targetValue = if (isFabExpanded) 45f else 0f,
        label = "FabRotation"
    )

    val transactions by viewModel.transactions.collectAsState()
    val userList by viewModel.users.collectAsState()
    val categoryList by viewModel.categories.collectAsState()
    val staffList by viewModel.staffList.collectAsState()
    val totalWeight by viewModel.totalWeightKg.collectAsState()
    val totalValue by viewModel.totalValueRp.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(successMessage, errorMessage) {
        successMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearMessages()
        }
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearMessages()
        }
    }

    val greetingText = remember {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when (hour) {
            in 4..10 -> "Selamat Pagi"
            in 11..14 -> "Selamat Siang"
            in 15..18 -> "Selamat Sore"
            else -> "Selamat Malam"
        }
    }

    val todayDateStr = remember {
        val sdf = SimpleDateFormat("EEEE, d MMMM", Locale("id", "ID"))
        sdf.format(Date())
    }

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
                                viewModel.loadDashboardData()
                                showTimbangBottomSheet = true
                            }
                        )

                        SmallExtendedFabItem(
                            text = "Pencairan",
                            icon = Icons.Default.Payments,
                            containerColor = MaterialTheme.colorScheme.primary,
                            onClick = {
                                isFabExpanded = false
                                viewModel.loadDashboardData()
                                showPencairanBottomSheet = true
                            }
                        )
                    }
                }

                FloatingActionButton(
                    onClick = { isFabExpanded = !isFabExpanded },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = RoundedCornerShape(16.dp)
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
                    text = greetingText,
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
                                text = todayDateStr,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(32.dp))
                            Text(
                                text = "${String.format(Locale("id", "ID"), "%.2f", totalWeight)}kg",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Rp${String.format(Locale("id", "ID"), "%,d", totalValue.toInt()).replace(',', '.')}",
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
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(transactions, key = { it.id }) { transaction ->
                HistoryCardItem(item = transaction)
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
                    userList = userList,
                    categoryList = categoryList,
                    staffList = staffList,
                    onDismiss = { showTimbangBottomSheet = false },
                    onSubmit = { userId, categoryId, staffId, weightKg ->
                        viewModel.createDeposit(userId, categoryId, staffId, weightKg) {
                            showTimbangBottomSheet = false
                        }
                    }
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
                    userList = userList,
                    staffList = staffList,
                    onDismiss = { showPencairanBottomSheet = false },
                    onSubmit = { userId, staffId, amount ->
                        viewModel.createWithdrawal(userId, staffId, amount) {
                            showPencairanBottomSheet = false
                        }
                    }
                )
            }
        }
    }
}

// Measure Bottom Sheet
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimbangBottomSheetContent(
    userList: List<UserUiModel>,
    categoryList: List<WasteCategoryUi>,
    staffList: List<AdminUi>,
    onDismiss: () -> Unit,
    onSubmit: (userId: String, categoryId: String, staffId: String, weightKg: Double) -> Unit
) {
    var selectedUser by remember { mutableStateOf<UserUiModel?>(null) }
    var selectedCategory by remember { mutableStateOf<WasteCategoryUi?>(null) }
    var selectedStaff by remember { mutableStateOf<AdminUi?>(staffList.firstOrNull()) }
    var beratGramInput by remember { mutableStateOf("") }

    var userDropdownExpanded by remember { mutableStateOf(false) }
    var categoryDropdownExpanded by remember { mutableStateOf(false) }
    var staffDropdownExpanded by remember { mutableStateOf(false) }

    val weightGrams = beratGramInput.toDoubleOrNull() ?: 0.0
    val weightKg = weightGrams / 1000.0

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

        // Dropdown Pilih Nasabah
        ExposedDropdownMenuBox(
            expanded = userDropdownExpanded,
            onExpandedChange = { userDropdownExpanded = !userDropdownExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedUser?.let { "${it.name} (Saldo: ${it.balance})" } ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Pilih Nasabah") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = userDropdownExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )
            ExposedDropdownMenu(
                expanded = userDropdownExpanded,
                onDismissRequest = { userDropdownExpanded = false }
            ) {
                userList.forEach { user ->
                    DropdownMenuItem(
                        text = { Text("${user.name} (${user.phoneNumber})") },
                        onClick = {
                            selectedUser = user
                            userDropdownExpanded = false
                        }
                    )
                }
            }
        }

        // Dropdown Kategori Sampah
        ExposedDropdownMenuBox(
            expanded = categoryDropdownExpanded,
            onExpandedChange = { categoryDropdownExpanded = !categoryDropdownExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedCategory?.let { "${it.name} (Rp${it.price}/${it.unit})" } ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Kategori Sampah") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )
            ExposedDropdownMenu(
                expanded = categoryDropdownExpanded,
                onDismissRequest = { categoryDropdownExpanded = false }
            ) {
                categoryList.forEach { category ->
                    DropdownMenuItem(
                        text = { Text("${category.name} - Rp${category.price}/${category.unit}") },
                        onClick = {
                            selectedCategory = category
                            categoryDropdownExpanded = false
                        }
                    )
                }
            }
        }

        // Dropdown Petugas
        if (staffList.isNotEmpty()) {
            ExposedDropdownMenuBox(
                expanded = staffDropdownExpanded,
                onExpandedChange = { staffDropdownExpanded = !staffDropdownExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedStaff?.fullName ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Petugas Penanggung Jawab") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = staffDropdownExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )
                ExposedDropdownMenu(
                    expanded = staffDropdownExpanded,
                    onDismissRequest = { staffDropdownExpanded = false }
                ) {
                    staffList.forEach { staff ->
                        DropdownMenuItem(
                            text = { Text("${staff.fullName} (@${staff.username})") },
                            onClick = {
                                selectedStaff = staff
                                staffDropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }

        OutlinedTextField(
            value = beratGramInput,
            onValueChange = { beratGramInput = it },
            label = { Text("Berat Sampah (gram / g)") },
            placeholder = { Text("Contoh: 500 (untuk 0.5 kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        // Live Auto Calculation Display
        if (selectedCategory != null && weightGrams > 0) {
            val priceClean = selectedCategory?.price?.replace(".", "")?.replace(",", ".")?.toDoubleOrNull() ?: 0.0
            val totalPrice = (weightKg * priceClean).toInt()

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Estimasi Total Pendapatan:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Rp${String.format(Locale("id", "ID"), "%,d", totalPrice).replace(',', '.')}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "(${String.format(Locale("id", "ID"), "%.3f", weightKg)} kg × Rp${selectedCategory?.price}/${selectedCategory?.unit})",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val userId = selectedUser?.userId ?: ""
                val categoryId = selectedCategory?.id ?: ""
                val staffId = selectedStaff?.id ?: "1"

                if (userId.isNotBlank() && categoryId.isNotBlank() && weightKg > 0) {
                    onSubmit(userId, categoryId, staffId, weightKg)
                }
            },
            enabled = selectedUser != null && selectedCategory != null && weightGrams > 0,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(100)
        ) {
            Text("Proses Setor", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PencairanBottomSheetContent(
    userList: List<UserUiModel>,
    staffList: List<AdminUi>,
    onDismiss: () -> Unit,
    onSubmit: (userId: String, staffId: String, amount: Double) -> Unit
) {
    var selectedUser by remember { mutableStateOf<UserUiModel?>(null) }
    var selectedStaff by remember { mutableStateOf<AdminUi?>(staffList.firstOrNull()) }
    var nominalInput by remember { mutableStateOf("") }

    var userDropdownExpanded by remember { mutableStateOf(false) }
    var staffDropdownExpanded by remember { mutableStateOf(false) }

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

        // Dropdown Pilih Nasabah
        ExposedDropdownMenuBox(
            expanded = userDropdownExpanded,
            onExpandedChange = { userDropdownExpanded = !userDropdownExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedUser?.let { "${it.name} (${it.balance})" } ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Pilih Nasabah") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = userDropdownExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )
            ExposedDropdownMenu(
                expanded = userDropdownExpanded,
                onDismissRequest = { userDropdownExpanded = false }
            ) {
                userList.forEach { user ->
                    DropdownMenuItem(
                        text = { Text("${user.name} - Saldo: ${user.balance}") },
                        onClick = {
                            selectedUser = user
                            userDropdownExpanded = false
                        }
                    )
                }
            }
        }

        // Dropdown Petugas
        if (staffList.isNotEmpty()) {
            ExposedDropdownMenuBox(
                expanded = staffDropdownExpanded,
                onExpandedChange = { staffDropdownExpanded = !staffDropdownExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedStaff?.fullName ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Petugas Penanggung Jawab") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = staffDropdownExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )
                ExposedDropdownMenu(
                    expanded = staffDropdownExpanded,
                    onDismissRequest = { staffDropdownExpanded = false }
                ) {
                    staffList.forEach { staff ->
                        DropdownMenuItem(
                            text = { Text("${staff.fullName} (@${staff.username})") },
                            onClick = {
                                selectedStaff = staff
                                staffDropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }

        OutlinedTextField(
            value = nominalInput,
            onValueChange = { nominalInput = it },
            label = { Text("Nominal Pencairan (Rp)") },
            placeholder = { Text("Masukkan nominal rupiah") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val amountDouble = nominalInput.toDoubleOrNull() ?: 0.0
                val userId = selectedUser?.userId ?: ""
                val staffId = selectedStaff?.id ?: "1"

                if (userId.isNotBlank() && amountDouble > 0) {
                    onSubmit(userId, staffId, amountDouble)
                }
            },
            enabled = selectedUser != null && (nominalInput.toDoubleOrNull() ?: 0.0) > 0,
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

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen(navController = rememberNavController())
    }
}
