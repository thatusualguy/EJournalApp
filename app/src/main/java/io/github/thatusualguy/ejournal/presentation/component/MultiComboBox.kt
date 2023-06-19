package io.github.thatusualguy.ejournal.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.thatusualguy.ejournal.presentation.theme.EJournalTheme

data class ComboOption(
    val text: String,
    val id: Int,
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MultiComboBox(
    labelText: String,
    options: List<ComboOption>,
    onOptionsChosen: (List<ComboOption>) -> Unit,
    modifier: Modifier = Modifier,
    selectedIds: List<Int> = emptyList(),
) {
    var expanded by remember { mutableStateOf(false) }
    // when no options available, I want ComboBox to be disabled
    val isEnabled by rememberUpdatedState { options.isNotEmpty() }
    val selectedOptionsList = remember { mutableStateListOf<Int>() }

    //Initial setup of selected ids
    selectedOptionsList.clear()
    selectedIds.forEach {
        selectedOptionsList.add(it)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            if (isEnabled()) {
                expanded = !expanded
                if (!expanded) {
                    onOptionsChosen(options.filter { it.id in selectedOptionsList }.toList())
                }
            }
        },
        modifier = modifier,
    ) {
        val selectedSummary = when (selectedOptionsList.size) {
            0 -> ""
            1 -> options.first { it.id == selectedOptionsList.first() }.text
            else -> "Выбрано ${selectedOptionsList.size}"
        }
//        var selectedSummary = ""
//        selectedIds.map { s -> options.find { it.id == s } }
//            .forEach { it?.text?.let { selectedSummary += "${it.substring(0, 8)}, " } }
        OutlinedTextField(
            enabled = isEnabled(),
            readOnly = true,
            value = selectedSummary,
            singleLine = true,
            onValueChange = {},
            label = { Text(text = labelText) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = modifier
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                onOptionsChosen(options.filter { it.id in selectedOptionsList }.toList())
            },
        ) {
            options.forEach {

                //use derivedStateOf to evaluate if it is checked
                val checked by remember {
                    derivedStateOf { selectedOptionsList.contains(it.id) }
                }


                DropdownMenuItem(
                    content = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = checked,
                                onCheckedChange = null
                            )
                            EJournalTheme {
                                Text(text = it.text)
                            }}
                    },
                    onClick = {
                        if (!checked) {
                            selectedOptionsList.add(it.id)
                        } else {
                            selectedOptionsList.remove(it.id)
                        }
                    },
                    modifier = modifier,
                    contentPadding = PaddingValues(8.dp)
                )
            }
        }
    }
}


@Composable
@Preview
fun MultiComboBoxPreview() {
    val options = listOf(
        ComboOption("Opt 1", 1),
        ComboOption("Opt 2", 2),
        ComboOption("Opt 3", 3),
        ComboOption("Opt 4", 4),
    )

    val selectedOptions = remember { mutableStateListOf<Int>() }

    EJournalTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                Text(
                    text = selectedOptions.size.toString(),
                    modifier = Modifier.clickable { selectedOptions.clear() })
                MultiComboBox(
                    labelText = "My combo box", options = options, onOptionsChosen = {
                        selectedOptions.clear()
                        it.forEach { selectedOptions.add(it.id) }
                    },
                    selectedIds = selectedOptions,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}