package com.example.makart


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp




@Composable
fun DrawingListItem(drawingEntity: DrawingEntity , onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .padding(16.dp, 8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            ) {
                Text(
                    text = drawingEntity.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "last Modified date",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Image(
                painter = painterResource(id = R.drawable.placeholder),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
            )
        }
    }
}

// Image(
//                bitmap = base64StringToBitmap(drawingEntity.thumbnail).asImageBitmap(),
//                contentDescription = null,
//                modifier = Modifier
//                    .size(100.dp)
//            )