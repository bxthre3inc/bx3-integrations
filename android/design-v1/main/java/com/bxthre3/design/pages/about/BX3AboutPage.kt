package com.bxthre3.design.pages.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bxthre3.design.atoms.BX3Divider
import com.bxthre3.design.atoms.BX3Text
import com.bxthre3.design.atoms.BX3TextStyle
import com.bxthre3.design.molecules.BX3Card
import com.bxthre3.design.organisms.BX3NavBar

data class TeamMember(val name: String, val role: String, val bio: String)

@Composable
fun BX3AboutPage(
    companyName: String,
    mission: String,
    description: String,
    team: List<TeamMember>,
    navItems: List<com.bxthre3.design.organisms.NavItem>,
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            BX3Text(
                text = companyName,
                style = BX3TextStyle.DisplayMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            BX3Text(
                text = mission,
                style = BX3TextStyle.HeadlineSmall,
                color = com.bxthre3.design.theme.BX3Colors.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            BX3Divider()
            Spacer(modifier = Modifier.height(24.dp))
            
            BX3Text(
                text = "About",
                style = BX3TextStyle.HeadlineMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            BX3Text(
                text = description,
                style = BX3TextStyle.BodyLarge
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            BX3Text(
                text = "Our Team",
                style = BX3TextStyle.HeadlineMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            team.forEach { member ->
                BX3Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        BX3Text(
                            text = member.name,
                            style = BX3TextStyle.BodyLarge
                        )
                        BX3Text(
                            text = member.role,
                            style = BX3TextStyle.LabelMedium,
                            color = com.bxthre3.design.theme.BX3Colors.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        BX3Text(
                            text = member.bio,
                            style = BX3TextStyle.BodyMedium
                        )
                    }
                }
            }
        }
        
        BX3NavBar(
            items = navItems,
            currentRoute = currentRoute,
            onNavigate = onNavigate
        )
    }
}
