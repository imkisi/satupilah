package com.example.satupilah.ui.screens.category

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun CategoryScreen(
    navController: NavController = rememberNavController(),
    viewModel: CategoryViewModel = viewModel()
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var showAddCategoryBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadCategories()
    }

    val categoryList by viewModel.categories.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
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

    val filteredCategories = remember(searchQuery, categoryList) {
        if (searchQuery.isBlank()) categoryList
        else categoryList.filter {
            it.name.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddCategoryBottomSheet = true },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.NoteAdd, contentDescription = "Tambah Kategori")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(bottom = padding.calculateBottomPadding())
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(top = 19.dp, bottom = 24.dp)
            ) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 0.dp, bottom = 8.dp)
                    ) {
                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Kembali",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Daftar Sampah",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Cari Sampah") },
                        trailingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        shape = RoundedCornerShape(28.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            unfocusedBorderColor = MaterialTheme.colorScheme.surfaceContainer
                        )
                    )
                }

                item { Spacer(modifier = Modifier.height(4.dp)) }

                if (filteredCategories.isEmpty() && !isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Tidak ada data kategori sampah",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    items(filteredCategories, key = { it.id }) { item ->
                        CategoryCardListItem(
                            category = item,
                            onClick = {
                                navController.navigate("category_detail/${item.id}")
                            }
                        )
                    }
                }
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        if (showAddCategoryBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showAddCategoryBottomSheet = false },
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            ) {
                AddCategoryBottomSheetContent(
                    onDismiss = { showAddCategoryBottomSheet = false },
                    onSubmit = { name, price ->
                        viewModel.createCategory(name, price) {
                            showAddCategoryBottomSheet = false
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun CategoryCardListItem(
    category: WasteCategoryUi,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = category.name,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            supportingContent = {
                Text(
                    text = "Rp${category.price} / ${category.unit}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            leadingContent = {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            },
            trailingContent = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            colors = ListItemDefaults.colors(
                containerColor = androidx.compose.ui.graphics.Color.Transparent
            )
        )
    }
}

@Composable
fun AddCategoryBottomSheetContent(
    onDismiss: () -> Unit,
    onSubmit: (name: String, price: Double) -> Unit
) {
    var categoryNameInput by remember { mutableStateOf("") }
    var priceInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Tambah Kategori Sampah",
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
            Text("Simpan Kategori", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryScreenPreview() {
    MaterialTheme {
        CategoryScreen(navController = rememberNavController())
    }
}
