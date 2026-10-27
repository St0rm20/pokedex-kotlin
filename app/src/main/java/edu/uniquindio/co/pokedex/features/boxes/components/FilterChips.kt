package edu.uniquindio.co.pokedex.features.boxes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.uniquindio.co.pokedex.domain.model.Generation
import edu.uniquindio.co.pokedex.domain.model.PokemonType
import edu.uniquindio.co.pokedex.ui.theme.PokedexInk
import edu.uniquindio.co.pokedex.ui.theme.PokedexRedDark
import edu.uniquindio.co.pokedex.ui.theme.PressStart2P

/** Fila de 40 dp con los selectores de tipo y generación, con el estilo del campo de búsqueda. */
@Composable
fun FilterChips(
    selectedType: PokemonType?,
    selectedGeneration: Generation?,
    onTypeSelected: (PokemonType?) -> Unit,
    onGenerationSelected: (Generation?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        FilterDropdown(
            label = "TIPO",
            selected = selectedType,
            options = PokemonType.entries,
            optionLabel = { it.label },
            onSelected = onTypeSelected,
            modifier = Modifier.weight(1f)
        )
        FilterDropdown(
            label = "GEN",
            selected = selectedGeneration,
            options = Generation.entries,
            optionLabel = { it.label },
            onSelected = onGenerationSelected,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun <T> FilterDropdown(
    label: String,
    selected: T?,
    options: List<T>,
    optionLabel: (T) -> String,
    onSelected: (T?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(34.dp)
                .background(Color(0xFFFBF6E2), RoundedCornerShape(4.dp))
                .border(3.dp, PokedexRedDark, RoundedCornerShape(4.dp))
                .clickable { expanded = true }
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (selected != null) optionLabel(selected).uppercase() else label,
                fontFamily = PressStart2P,
                fontSize = 8.sp,
                color = PokedexInk,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Text(text = "▾", fontSize = 12.sp, color = PokedexInk)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text("Todos") },
                onClick = {
                    onSelected(null)
                    expanded = false
                }
            )
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(optionLabel(option)) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
