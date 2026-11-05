package com.example.satupilah.ui.screens.category

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.satupilah.data.model.WasteCategoryUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailScreen(
    categoryId: String = "1",
    navController: NavController = rememberNavController(),
    viewModel: CategoryViewModel = viewModel()
) {
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditBottomSheet by remember { mutableStateOf(false) }

    val categoryList by viewModel.categories.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(successMessage, errorMessage) {
        successMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearMessages()
        }
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearMessages()
        }
    }

    val category = categoryList.find { it.id == categoryId } ?: WasteCategoryUi(
        id = categoryId,
        name = "Botol Plastik",
        price = "2.000",
        unit = "kg"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Sampah", fontWeight = FontWeight.Normal) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(72.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.DeleteSweep,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = category.name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = "ID Kategori: #${category.id}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DetailRowItem(
                        label = "Harga Beli per Satuan",
                        value = "Rp${category.price} / ${category.unit}",
                        isHighlight = true
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    DetailRowItem(label = "Kategori Sampah", value = category.name)
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    DetailRowItem(label = "Satuan Ukur", value = "Kilogram (kg)")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { showEditBottomSheet = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(100),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit Harga", fontWeight = FontWeight.SemiBold)
                }

                OutlinedButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(100),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.error)
                    )
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Hapus", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }

    if (showEditBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showEditBottomSheet = false },
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            EditCategoryBottomSheetContent(
                currentCategory = category,
                onDismiss = { showEditBottomSheet = false },
                onSubmit = { name, price ->
                    viewModel.updateCategory(category.id, name, price) {
                        showEditBottomSheet = false
                    }
                }
            )
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus Kategori Sampah") },
            text = { Text("Apakah Anda yakin ingin menghapus kategori '${category.name}'? Transaksi terkait kategori ini dapat terpengaruh.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteCategory(category.id) {
                            showDeleteDialog = false
                            navController.popBackStack()
                        }
                    }
                ) {
                    Text("Hapus", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}

@Composable
fun EditCategoryBottomSheetContent(
    currentCategory: WasteCategoryUi,
    onDismiss: () -> Unit,
    onSubmit: (name: String, price: Double) -> Unit
) {
    var categoryNameInput by remember { mutableStateOf(currentCategory.name) }
    var priceInput by remember { mutableStateOf(currentCategory.price.replace(".", "").replace(",", ".")) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Edit Kategori Sampah",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        OutlinedTextField(
            value = categoryNameInput,
            onValueChange = { categoryNameInput = it },
            label = { Text("Nama Kategori") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        OutlinedTextField(
            value = priceInput,
            onValueChange = { priceInput = it },
            label = { Text("Harga per kg (Rp)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val priceDouble = priceInput.toDoubleOrNull() ?: 0.0
                if (categoryNameInput.isNotBlank() && priceDouble > 0) {
                    onSubmit(categoryNameInput.trim(), priceDouble)
                }
            },
            enabled = categoryNameInput.isNotBlank() && priceInput.toDoubleOrNull() != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(100)
        ) {
            Text("Simpan Perubahan", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun DetailRowItem(label: String, value: String, isHighlight: Boolean = false) {
    Column {
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            fontSize = if (isHighlight) 18.sp else 15.sp,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Medium,
            color = if (isHighlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryDetailScreenPreview() {
    MaterialTheme {
        CategoryDetailScreen(navController = rememberNavController())
    }
}
