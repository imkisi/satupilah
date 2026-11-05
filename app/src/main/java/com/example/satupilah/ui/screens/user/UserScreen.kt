package com.example.satupilah.ui.screens.user

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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
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
import com.example.satupilah.data.model.UserUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    navController: NavController = rememberNavController(),
    viewModel: UserViewModel = viewModel()
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var showAddUserBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadUsers()
    }

    val userList by viewModel.users.collectAsState()
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

    val filteredUsers = remember(searchQuery, userList) {
        if (searchQuery.isBlank()) userList
        else userList.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
            it.phoneNumber.contains(searchQuery, ignoreCase = true) ||
            it.address.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddUserBottomSheet = true },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = "Tambah Nasabah")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(top = 19.dp, bottom = 24.dp)
            ) {
                item {
                    Text(
                        text = "Daftar Pelanggan",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Cari Pelanggan") },
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

                if (filteredUsers.isEmpty() && !isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Tidak ada data nasabah",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    items(filteredUsers, key = { it.userId }) { user ->
                        UserCardListItem(
                            user = user,
                            onClick = {
                                navController.navigate("user_profile/${user.userId}")
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

        if (showAddUserBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showAddUserBottomSheet = false },
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            ) {
                AddUserBottomSheetContent(
                    onDismiss = { showAddUserBottomSheet = false },
                    onSubmit = { name, phone, address ->
                        viewModel.createUser(name, phone, address) {
                            showAddUserBottomSheet = false
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun AddUserBottomSheetContent(
    onDismiss: () -> Unit,
    onSubmit: (name: String, phone: String, address: String) -> Unit
) {
    var nameInput by remember { mutableStateOf("") }
    var phoneInput by remember { mutableStateOf("") }
    var addressInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Registrasi Nasabah Baru",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        OutlinedTextField(
            value = nameInput,
            onValueChange = { nameInput = it },
            label = { Text("Nama Lengkap") },
            placeholder = { Text("Masukkan nama nasabah") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        OutlinedTextField(
            value = phoneInput,
            onValueChange = { phoneInput = it },
            label = { Text("Nomor HP / WhatsApp") },
            placeholder = { Text("Contoh: 081234567890") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        OutlinedTextField(
            value = addressInput,
            onValueChange = { addressInput = it },
            label = { Text("Alamat Tempat Tinggal") },
            placeholder = { Text("Masukkan alamat lengkap") },
            maxLines = 3,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (nameInput.isNotBlank() && phoneInput.isNotBlank()) {
                    onSubmit(nameInput.trim(), phoneInput.trim(), addressInput.trim())
                }
            },
            enabled = nameInput.isNotBlank() && phoneInput.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(100)
        ) {
            Text("Simpan Nasabah", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun UserCardListItem(
    user: UserUiModel,
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
                    text = user.name,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            supportingContent = {
                Text(
                    text = "${user.phoneNumber} • Saldo: ${user.balance}",
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
                            imageVector = Icons.Default.Person,
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

@Preview(showBackground = true)
@Composable
fun UserScreenPreview() {
    MaterialTheme {
        UserScreen()
    }
}
