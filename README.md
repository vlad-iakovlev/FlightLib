# Flight Lib

This is a library that provides encapsulates the logic used by the [Create Jetpack](https://github.com/PssbleTrngle/CreateJetpack) mod to be reusable

## Using the library

The packages are hosted on GitHub

```kotlin
repositories {
    maven { url = uri("https://maven.pkg.github.com/PssbleTrngle/FlightLib") }
}
```

### For Forge

```kotlin
dependencies {
    compileOnly(fg.deobf("com.possible-triangle:flightlib-api:${flightlib_version}"))
    compileOnly(fg.deobf("com.possible-triangle:flightlib-forge-api:${flightlib_version}"))
    runtimeOnly(fg.deobf("com.possible-triangle:flightlib-forge:${flightlib_version}"))
    
    jarJar("com.possible-triangle:flightlib-forge:${flightlib_version}") {
        jarJar.ranged(this, "[${flightlib_version},)")
    }
}
```

In order to register a jetpack, attach the [Capability](https://docs.minecraftforge.net/en/latest/datastorage/capabilities/)
to either the ItemStack, or the Player themselves. The `IJetpack` Capability can be found at under `ForgeFlightLib.JETPACK_CAPABILITY`.
