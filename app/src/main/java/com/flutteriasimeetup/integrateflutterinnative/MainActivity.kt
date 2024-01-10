package com.flutteriasimeetup.integrateflutterinnative

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flutteriasimeetup.integrateflutterinnative.ui.theme.IntegrateFlutterInNativeTheme
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel


const val SINGLE_FLUTTER_ENGINE_ID = "com.integrateFlutterInNative.engineID"
class MainActivity : ComponentActivity() {

    private lateinit var flutterEngine: FlutterEngine
    private lateinit var methodChannel: MethodChannel
    private fun setUpFlutterEngine() {
        flutterEngine = FlutterEngine(this)
        // Start executing Dart code to pre-warm the FlutterEngine.
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )

        // Cache the FlutterEngine
        FlutterEngineCache
            .getInstance()
            .put(SINGLE_FLUTTER_ENGINE_ID, flutterEngine)

        methodChannel = MethodChannel(flutterEngine.dartExecutor, "com.integrateFlutterInNative.channel")
        methodChannel.setMethodCallHandler { call, result -> handleHostChannelMethods(call, result) }
    }
    private fun handleHostChannelMethods(
        call: MethodCall,
        result: MethodChannel.Result
    ) {
        when (call.method) {
            "getToken" -> getToken(result)
            "dismiss" -> dismiss(result)
            else -> result.notImplemented()
        }
    }
    private fun getToken(result: MethodChannel.Result) {
        return result.success("f2722e1bf7faebdd5e63810b446bf499")
    }
    private fun dismiss(result: MethodChannel.Result) {
        return result.success(true)
    }
    private fun incrementCounter() {
        methodChannel.invokeMethod("incrementCounter",null)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpFlutterEngine()

        setContent {
            IntegrateFlutterInNativeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Greeting("Flutter")

                        Button(
                            onClick = {
                                startActivity(
                                    FlutterActivity.withCachedEngine(SINGLE_FLUTTER_ENGINE_ID)
                                        .build(this@MainActivity)
                                )
                            },
                            modifier = Modifier
                                .height(40.dp)
                                .fillMaxWidth()
                        ) {
                            Text("Go to Flutter")
                        }

                        // New Increment Counter Button
                        Button(
                            onClick = {
                                incrementCounter()
                            },
                            modifier = Modifier
                                .height(40.dp)
                                .fillMaxWidth()
                        ) {
                            Text("Increment Counter")
                        }

                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    IntegrateFlutterInNativeTheme {
        Greeting("Android")
    }
}