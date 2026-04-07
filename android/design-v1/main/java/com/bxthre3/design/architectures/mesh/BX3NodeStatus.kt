package com.bxthre3.design.architectures.mesh

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bxthre3.design.atoms.BX3Badge
import com.bxthre3.design.atoms.BX3Text
import com.bxthre3.design.atoms.BX3TextStyle
import com.bxthre3.design.molecules.BX3Card
import com.bxthre3.design.theme.BX3Colors

data class MeshNode(val id: String, val label: String, val status: MeshNodeStatus, val latency: Int)

enum class MeshNodeStatus { ONLINE, OFFLINE, DEGRADED, SYNCING }

@Composable
fun BX3NodeStatusCard(
    node: MeshNode,
    modifier: Modifier = Modifier
) {
    val statusColor = when (node.status) {
        MeshNodeStatus.ONLINE -> BX3Colors.trendUp
        MeshNodeStatus.OFFLINE -> BX3Colors.error
        MeshNodeStatus.DEGRADED -> BX3Colors.trendDown
        MeshNodeStatus.SYNCING -> BX3Colors.info
    }
    
    BX3Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BX3Badge(
                count = 0,
                modifier = Modifier
                    .size(12.dp)
                    .background(statusColor, CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            BX3Text(
                text = node.label,
                style = BX3TextStyle.BodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            BX3Text(
                text = "${node.latency}ms",
                style = BX3TextStyle.LabelMedium,
                color = BX3Colors.onSurfaceVariant
            )
        }
    }
}
