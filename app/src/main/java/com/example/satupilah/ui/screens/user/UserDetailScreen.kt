package com.example.satupilah.ui.screens.user

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
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
import com.example.satupilah.data.model.UserUiModel
import com.example.satupilah.ui.screens.category.DetailRowItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    userId: String = "1",
    navController: NavController = rememberNavController(),
    viewModel: UserViewModel = viewModel()
) {
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditBottomSheet by remember { mutableStateOf(false) }

    val userList by viewModel.users.collectAsState()
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

    val user = userList.find { it.userId == userId } ?: UserUiModel(
        userId = userId,
        name = "Jane Doe Abdulsalam",
        phoneNumber = "+6281234567890",
        address = "Jl. Merdeka No. 12, Kel. Kebon Jeruk, Bandung",
        balance = "Rp120.000",
        createdAt = "23 September 2026"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Pelanggan", fontWeight = FontWeight.Normal) },
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
            // Header Kartu Profil Nasabah
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
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
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = user.name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "ID Nasabah: #${user.userId}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
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
                    DetailRowItem(label = "Nomor HP / WhatsApp", value = user.phoneNumber)
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    DetailRowItem(label = "Alamat Lengkap", value = user.address)
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    DetailRowItem(label = "Saldo Aktif", value = user.balance, isHighlight = true)
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    DetailRowItem(label = "Terdaftar Pada", value = user.createdAt)
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
                    Text("Edit Data", fontWeight = FontWeight.SemiBold)
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
            EditUserBottomSheetContent(
                currentUser = user,
                onDismiss = { showEditBottomSheet = false },
                onSubmit = { name, phone, address ->
                    viewModel.updateUser(user.userId, name, phone, address) {
                        showEditBottomSheet = false
                    }
                }
            )
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus Nasabah") },
            text = { Text("Apakah Anda yakin ingin menghapus data nasabah ${user.name}? Tindakan ini tidak dapat dibatalkan.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteUser(user.userId) {
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
fun EditUserBottomSheetContent(
    currentUser: UserUiModel,
    onDismiss: () -> Unit,
    onSubmit: (name: String, phone: String, address: String) -> Unit
) {
    var nameInput by remember { mutableStateOf(currentUser.name) }
    var phoneInput by remember { mutableStateOf(currentUser.phoneNumber) }
    var addressInput by remember { mutableStateOf(currentUser.address) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Edit Data Nasabah",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        OutlinedTextField(
            value = nameInput,
            onValueChange = { nameInput = it },
            label = { Text("Nama Lengkap") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        OutlinedTextField(
            value = phoneInput,
            onValueChange = { phoneInput = it },
            label = { Text("Nomor HP / WhatsApp") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        OutlinedTextField(
            value = addressInput,
            onValueChange = { addressInput = it },
            label = { Text("Alamat Tempat Tinggal") },
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
            Text("Simpan Perubahan", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserDetailScreenPreview() {
    MaterialTheme {
        UserDetailScreen(navController = rememberNavController())
    }
}
