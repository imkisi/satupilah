package com.example.satupilah.ui.screens.user

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// Model data sesuai atribut tabel database 'users'
data class UserUiModel(
    val userId: String,
    val name: String,
    val phoneNumber: String,
    val address: String,
    val balance: String,
    val createdAt: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    navController: NavController = rememberNavController()
) {
    var searchQuery by remember { mutableStateOf("") }

    // State untuk kontrol visibilitas Bottom Sheet Tambah Nasabah
    var showAddUserBottomSheet by remember { mutableStateOf(false) }

    // Sample data yang merepresentasikan record tabel database 'users'
    val sampleUsers = listOf(
        UserUiModel("1", "Jane Doe Abdulsalam", "+6281234567890", "Jl. Merdeka No. 12, Bandung", "Rp120.000", "23 Sep 2026"),
        UserUiModel("2", "Budi Santoso", "+6289876543210", "Jl. Mawar No. 45, Jakarta", "Rp45.500", "20 Sep 2026"),
        UserUiModel("3", "Siti Aminah", "+6281122334455", "Jl. Anggrek No. 8, Surabaya", "Rp210.000", "15 Sep 2026"),
        UserUiModel("4", "Ahmad Fauzi", "+6285566778899", "Jl. Diponegoro No. 88, Semarang", "Rp15.000", "10 Sep 2026"),
        UserUiModel("5", "Dewi Lestari", "+6287788990011", "Jl. Gajah Mada No. 3, Yogyakarta", "Rp88.000", "01 Sep 2026")
    )

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddUserBottomSheet = true }, // Membuka Bottom Sheet Form Tambah Nasabah
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                shape = CircleShape
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = "Tambah Nasabah")
            }
        }
    ) { padding ->
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
                    text = "Daftar Nasabah",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Cari Nasabah") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    shape = RoundedCornerShape(28.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        unfocusedBorderColor = MaterialTheme.colorScheme.surfaceContainer
                    )
                )
            }

            item { Spacer(modifier = Modifier.height(4.dp)) }

            // Menggunakan ListItem M3 untuk setiap item nasabah
            items(sampleUsers) { user ->
                UserCardListItem(
                    user = user,
                    onClick = {
                        navController.navigate("user_profile/${user.userId}")
                    }
                )
            }
        }

        // Pop-up Bottom Sheet: Form Tambah Nasabah Baru
        if (showAddUserBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showAddUserBottomSheet = false },
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            ) {
                AddUserBottomSheetContent(
                    onDismiss = { showAddUserBottomSheet = false }
                )
            }
        }
    }
}

// Komponen Form Pop-Up Tambah Nasabah (Material Design 3)
@Composable
fun AddUserBottomSheetContent(onDismiss: () -> Unit) {
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

        // Input Nama Lengkap
        OutlinedTextField(
            value = nameInput,
            onValueChange = { nameInput = it },
            label = { Text("Nama Lengkap") },
            placeholder = { Text("Masukkan nama nasabah") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        // Input Nomor Telepon / WA
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

        // Input Alamat
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

        // Tombol Simpan
        Button(
            onClick = {
                // TODO: Panggil ViewModel/API untuk INSERT INTO users
                onDismiss()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(100)
        ) {
            Text("Simpan Nasabah", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

// Komponen ListItem Material Design 3
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
                    fontWeight = FontWeight.SemiBold,
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
