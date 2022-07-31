package com.jefisu.hashgenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.jefisu.hashgenerator.destinations.HashGeneratedScreenDestination
import com.jefisu.hashgenerator.ui.theme.AzureRadiance
import com.jefisu.hashgenerator.ui.theme.Downriver
import com.jefisu.hashgenerator.ui.theme.Midnight
import com.jefisu.hashgenerator.ui.theme.Turquoise
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.dependency
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalMaterialNavigationApi
@ExperimentalMaterial3Api
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value
            }
        }
        setContent {
            val snackBarHostState = remember { SnackbarHostState() }
            val navHostEngine = rememberAnimatedNavHostEngine(
                rootDefaultAnimations = RootNavGraphDefaultAnimations(
                    enterTransition = { fadeIn(animationSpec = tween(700)) },
                    exitTransition = { fadeOut(animationSpec = tween(700)) },
                    popEnterTransition = { slideInVertically() + fadeIn(animationSpec = tween(700)) },
                )
            )
            Scaffold(
                snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
                containerColor = Midnight
            ) { innerPadding ->
                DestinationsNavHost(
                    engine = navHostEngine,
                    navGraph = NavGraphs.root,
                    dependenciesContainerBuilder = {
                        dependency(snackBarHostState)
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@RootNavGraph(start = true)
@Destination
@Composable
fun MainScreen(
    snackBarHostState: SnackbarHostState,
    navigator: DestinationsNavigator
) {
    val algorithms = stringArrayResource(R.array.algorithms)
    var text by remember { mutableStateOf("") }
    var selectedAlgorithm by remember { mutableStateOf(algorithms[0]) }
    val scope = rememberCoroutineScope()
    var isNavigating by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = isNavigating) {
        if (isNavigating) {
            delay(500)
            navigator.navigate(
                HashGeneratedScreenDestination(NavArguments(text, selectedAlgorithm))
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.align(Alignment.Center)
        ) {
            AnimatedVisibility(
                visible = !isNavigating,
                enter = fadeIn(animationSpec = tween(500)),
                exit = fadeOut(animationSpec = tween(500)),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "HASH \nGENERATOR",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center
                )
            }
            AnimatedVisibility(
                visible = !isNavigating,
                enter = slideInHorizontally(animationSpec = tween(500)) + fadeIn(),
                exit = slideOutHorizontally(animationSpec = tween(500)) + fadeOut()
            ) {
                ExposedDropDown(
                    algorithms = algorithms.asList(),
                    selectedAlgorithm = selectedAlgorithm,
                    onClickSelectedAlgorithm = { selectedAlgorithm = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            AnimatedVisibility(
                visible = !isNavigating,
                enter = slideInHorizontally(
                    animationSpec = tween(500),
                    initialOffsetX = { it }) + fadeIn(),
                exit = slideOutHorizontally(
                    animationSpec = tween(500),
                    targetOffsetX = { it }) + fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(MaterialTheme.shapes.extraSmall)
                        .background(Downriver)
                ) {
                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        textStyle = MaterialTheme.typography.bodyLarge,
                        maxLines = 7,
                        placeholder = { Text(text = "Text here...") },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            placeholderColor = Color.LightGray,
                            textColor = Color.White
                        )
                    )
                    if (text.isNotBlank()) {
                        IconButton(
                            onClick = { text = "" },
                            modifier = Modifier.align(Alignment.BottomEnd),
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(48.dp))
        }
        AnimatedVisibility(
            visible = !isNavigating,
            enter = fadeIn(animationSpec = tween(500)),
            exit = fadeOut(animationSpec = tween(500)),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Button(
                onClick = {
                    if (text.isBlank() || text.isEmpty()) {
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                message = "Could not generate hash. Please try again.",
                                actionLabel = "OK"
                            )
                        }
                        return@Button
                    }
                    isNavigating = true
                },
                shape = MaterialTheme.shapes.extraSmall,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzureRadiance,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "GENERATE",
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Destination(navArgsDelegate = NavArguments::class)
@Composable
fun HashGeneratedScreen(
    navigator: DestinationsNavigator,
    viewModel: MainViewModel = viewModel()
) {
    val clipboardManager = LocalClipboardManager.current
    var copiedText by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = copiedText) {
        if (copiedText) {
            delay(1500)
            copiedText = false
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        IconButton(
            onClick = navigator::navigateUp,
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = Color.White
            )
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null
            )
        }
        AnimatedVisibility(
            visible = copiedText,
            enter = slideInVertically(animationSpec = tween(300)) + fadeIn(),
            exit = slideOutVertically(animationSpec = tween(300)) + fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AzureRadiance)
                    .padding(8.dp)
            ) {
                Text(
                    text = "Copied!",
                    color = Color.White
                )
            }
        }
        Text(
            text = viewModel.hash,
            color = Turquoise,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 32.dp)
        )
        Button(
            onClick = {
                clipboardManager.setText(AnnotatedString(viewModel.hash))
                copiedText = true
            },
            shape = MaterialTheme.shapes.extraSmall,
            colors = ButtonDefaults.buttonColors(
                containerColor = AzureRadiance,
                contentColor = Color.White
            ),
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .height(50.dp)
                .align(Alignment.BottomCenter)
        ) {
            Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "COPY",
                fontSize = 18.sp
            )
        }
    }
}