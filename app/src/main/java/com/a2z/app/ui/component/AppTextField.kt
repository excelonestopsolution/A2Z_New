package com.a2z.app.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.a2z.app.R
import com.a2z.app.ui.screen.utility.util.DateTransformation
import com.a2z.app.ui.theme.RedColor
import com.a2z.app.ui.theme.spacing
import com.a2z.app.ui.util.resource.FormErrorType
import com.a2z.app.util.FormFieldError
import com.a2z.app.util.VoidCallback
import com.a2z.app.util.extension.insertDateSeparator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppTextField(
    value: String,
    label: String,
    hint: String = "Required*",
    onChange: (String) -> Unit = {},
    maxLength: Int = 256,
    leadingIcon: ImageVector = Icons.Filled.Input,
    trailingIcon: @Composable () -> Unit = {},
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardCapitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    error: FormFieldError = FormErrorType.Initial,
    isOutline: Boolean = false,
    readOnly: Boolean = false,
    topSpace: Dp = MaterialTheme.spacing.small,
    width: Dp? = null,
    textStyle: TextStyle = TextStyle.Default.copy(
        fontSize = 14.sp, fontWeight = FontWeight.SemiBold
    ),
    labelTextStyle: TextStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal),
    hintTextStyle: TextStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal),
    downText: String? = null,
    interactionSource: MutableInteractionSource = remember {
        MutableInteractionSource()
    }

) {

    val coroutineScope = rememberCoroutineScope()
    val bringIntoViewRequester = BringIntoViewRequester()


    val errorMsg = when (error) {
        is FormErrorType.Initial -> null
        is FormErrorType.Success -> null
        is FormErrorType.Failure<String> -> error.msg
    }


    val mLabel = @Composable {
        Text(text = label, style = labelTextStyle)
    }

    val mHint = @Composable {
        Text(text = hint, style = hintTextStyle)
    }
    val mLeadingIcon = @Composable {
        Icon(imageVector = leadingIcon, contentDescription = null)
    }

    val colors = TextFieldDefaults.textFieldColors(
        backgroundColor = Color.Transparent,
    )

    val modifier = Modifier
        .fillMaxWidth()
        .bringIntoViewRequester(bringIntoViewRequester)
        .onFocusEvent {
            if (it.isFocused || it.hasFocus) {
                coroutineScope.launch {
                    delay(250)
                    bringIntoViewRequester.bringIntoView()
                }
            }
        }
    val keyboardOptions = KeyboardOptions(
        keyboardType = keyboardType, capitalization = keyboardCapitalization
    )

    val focusRequester = remember { FocusRequester() }


    modifier.focusRequester(focusRequester)

    @Composable
    fun LocalTextField() {
        if (isOutline) {
            OutlinedTextField(
                value = value,
                textStyle = textStyle,
                onValueChange = {
                    if (maxLength != it.length - 1) onChange(it)
                },

                label = mLabel,
                colors = colors,
                placeholder = mHint,
                isError = errorMsg != null,
                leadingIcon = mLeadingIcon,
                trailingIcon = trailingIcon,
                modifier = modifier,
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions,
                singleLine = true,
                readOnly = readOnly,
                interactionSource = interactionSource

            )
        } else {
            TextField(
                value = value,
                onValueChange = {
                    if (maxLength != it.length - 1) onChange(it)
                },
                textStyle = textStyle,
                label = mLabel,
                colors = colors,
                placeholder = mHint,
                isError = errorMsg != null,
                leadingIcon = mLeadingIcon,
                trailingIcon = trailingIcon,
                modifier = modifier,
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions,
                singleLine = true,
                readOnly = readOnly,
                interactionSource = interactionSource

            )
        }
    }

    Column {

        Spacer(modifier = Modifier.height(topSpace))
        val boxModifier = if (width == null) Modifier.fillMaxWidth() else Modifier.width(width)
        Box(modifier = boxModifier) {
            LocalTextField()
        }
        if (errorMsg.orEmpty().isNotEmpty()) {
            Text(
                text = errorMsg.toString(),
                style = TextStyle(
                    color = RedColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W500
                ),
                modifier = Modifier.padding(4.dp)
            )
        }
        if (errorMsg.orEmpty().isEmpty() && downText != null) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = downText, style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W500,
                    color = Color.Black.copy(alpha = 0.6f)
                )
            )
        }
    }

}

@Composable
fun MobileTextField(
    value: String,
    onChange: (String) -> Unit,
    isOutline: Boolean = false,
    error: FormFieldError = FormErrorType.Initial,
    topSpace: Dp = MaterialTheme.spacing.small,
    downText: String? = "Enter 10 digits mobile number",
    trailingIcon: @Composable () -> Unit = {}
) {

    AppTextField(value = value,
        label = "Mobile Number",
        onChange = onChange,
        keyboardType = KeyboardType.Number,
        leadingIcon = Icons.Default.Phone,
        error = error,
        maxLength = 10,
        isOutline = isOutline,
        topSpace = topSpace,
        downText = downText,
        trailingIcon = { trailingIcon() }

    )
}


@Composable
fun EmailTextField(
    value: String,
    onChange: (String) -> Unit,
    isOutline: Boolean = false,
    error: FormFieldError = FormErrorType.Initial,
    topSpace: Dp = MaterialTheme.spacing.small,
    downText: String? = null
) {

    AppTextField(
        value = value,
        label = "Email Id",
        onChange = onChange,
        keyboardType = KeyboardType.Email,
        leadingIcon = Icons.Default.Email,
        error = error,
        maxLength = 28,
        isOutline = isOutline,
        topSpace = topSpace,
        downText = downText

    )
}


@Composable
fun DateTextField(
    value: String,
    label: String,
    onChange: (String) -> Unit,
    isOutline: Boolean = false,
    error: FormFieldError = FormErrorType.Initial,
    topSpace: Dp = MaterialTheme.spacing.small,
    downText: String? = null,
    onDateSelected: (String) -> Unit
) {

    val datePicker = rememberSaveable { mutableStateOf(false) }
    if (datePicker.value) DatePickerDialog(onCancelled = { datePicker.value = false },
        preSelectedDate = value.insertDateSeparator(),
        onDatePicked = {
            datePicker.value = false
            onDateSelected(it)
        })


    AppTextField(value = value,
        label = label,
        onChange = onChange,
        keyboardType = KeyboardType.Number,
        leadingIcon = Icons.Default.CalendarToday,
        error = error,
        maxLength = 8,
        isOutline = isOutline,
        topSpace = topSpace,
        visualTransformation = DateTransformation(),
        downText = downText,
        trailingIcon = {
            IconButton(onClick = {
                datePicker.value = true
            }) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
            }
        })
}


@Composable
fun PasswordTextField(
    value: String,
    onChange: (String) -> Unit,
    isOutline: Boolean = false,
    error: FormFieldError = FormErrorType.Initial,
    topSpace: Dp = MaterialTheme.spacing.medium
) {

    var passwordVisibility by rememberSaveable { mutableStateOf(true) }

    val toggleIcon = @Composable {
        IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
            Icon(
                painter = painterResource(
                    id = if (passwordVisibility) R.drawable.ic_baseline_visibility_24
                    else R.drawable.ic_baseline_visibility_off_24
                ),
                contentDescription = null,
            )
        }
    }

    AppTextField(
        value = value,
        hint = "********",
        label = "Password",
        onChange = onChange,
        keyboardType = KeyboardType.Password,
        leadingIcon = Icons.Default.Lock,
        trailingIcon = toggleIcon,
        error = error,
        visualTransformation = if (passwordVisibility) PasswordVisualTransformation()
        else VisualTransformation.None,
        isOutline = isOutline,
        topSpace = topSpace

    )
}


@Composable
fun PinTextField(
    value: String,
    mpin: Boolean = false,
    maxLength: Int = 6,
    onChange: (String) -> Unit,
    isOutline: Boolean = false,
    error: FormFieldError = FormErrorType.Initial,
    topSpace: Dp = MaterialTheme.spacing.small
) {

    var passwordVisibility by rememberSaveable { mutableStateOf(true) }

    val toggleIcon = @Composable {
        IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
            Icon(
                painter = painterResource(
                    id = if (passwordVisibility) R.drawable.ic_baseline_visibility_24
                    else R.drawable.ic_baseline_visibility_off_24
                ),
                contentDescription = null,
            )
        }
    }

    AppTextField(
        value = value,
        hint = "******",
        label = if (mpin) "M-PIN" else "OTP",
        onChange = onChange,
        keyboardType = KeyboardType.Number,
        leadingIcon = Icons.Default.Password,
        trailingIcon = toggleIcon,
        error = error,
        maxLength = maxLength,
        isOutline = isOutline,
        visualTransformation = if (passwordVisibility) PasswordVisualTransformation()
        else VisualTransformation.None,
        topSpace = topSpace

    )
}

@Composable
fun AmountTextField(
    value: String,
    onChange: (String) -> Unit = {},
    isOutline: Boolean = false,
    error: FormFieldError = FormErrorType.Initial,
    topSpace: Dp = MaterialTheme.spacing.small,
    downText: String? = null,
    readOnly: Boolean = false
) {

    AppTextField(
        value = value,
        hint = "Enter amount",
        label = "Amount",
        onChange = onChange,
        keyboardType = KeyboardType.Number,
        leadingIcon = Icons.Default.Input,
        error = error,
        maxLength = 8,
        isOutline = isOutline,
        topSpace = topSpace,
        textStyle = TextStyle.Default.copy(
            fontSize = 24.sp, fontWeight = FontWeight.Bold
        ),
        hintTextStyle = TextStyle.Default.copy(
            fontSize = 20.sp, fontWeight = FontWeight.SemiBold
        ),
        downText = downText,
        readOnly = readOnly

    )
}

@Composable
fun DropDownTextField(
    value: String,
    paddingValues: PaddingValues = PaddingValues(vertical = 4.dp, horizontal = 5.dp),
    hint: String,
    onClick: VoidCallback
) {
    val source = remember {
        MutableInteractionSource()
    }

    if (source.collectIsPressedAsState().value) onClick()
    Box(
        Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(paddingValues)
    ) {
        TextField(
            value = value,
            onValueChange = {},
            textStyle = TextStyle.Default.copy(
                fontWeight = FontWeight.SemiBold, fontSize = 16.sp
            ),
            placeholder = { Text(text = hint) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Blue.copy(alpha = 0.1f),

                focusedIndicatorColor = Color.Transparent, //hide the indicator
                unfocusedIndicatorColor = Color.Transparent
            ),
            trailingIcon = {
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)

            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.None,
            ),
            readOnly = true,
            interactionSource = source
        )
    }
}


@Composable
fun FileTextField(
    value: String,
    label: String,
    hint: String,
    error: FormFieldError = FormErrorType.Initial,
    onTrailingClick: VoidCallback = {},
    onClick: VoidCallback,

) {


    val source = remember {
        MutableInteractionSource()
    }
    if (source.collectIsPressedAsState().value) {
       onClick.invoke()
    }
    AppTextField(
        value = value,
        label = label,
        hint = hint,
        interactionSource = source,
        readOnly = true,
        leadingIcon = Icons.Default.FileUpload,
        error = error,
        trailingIcon = {
            if(value.isNotEmpty())  IconButton(onClick = onTrailingClick) {
                Icon(imageVector = Icons.Default.Image, contentDescription = null)
            }
        }
    )
}

@Composable
fun SearchTextField(
    value: String,
    paddingValues: PaddingValues = PaddingValues(vertical = 4.dp, horizontal = 5.dp),
    onQuery: (String) -> Unit,
    onClear: () -> Unit,
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(paddingValues)
    ) {
        TextField(
            value = value,
            onValueChange = {
                onQuery(it)
            },
            textStyle = TextStyle.Default,


            placeholder = { Text(text = "Search") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Blue.copy(alpha = 0.1f),

                focusedIndicatorColor = Color.Transparent, //hide the indicator
                unfocusedIndicatorColor = Color.Transparent
            ),
            trailingIcon = {
                if (value.isEmpty()) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                } else IconButton(onClick = {
                    onClear()
                }) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.None,
            ),
        )
    }
}