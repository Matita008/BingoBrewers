plugins {
    // Applies net.fabricmc.fabric-loom-remap on obfuscated versions (1.21.11) and
    // net.fabricmc.fabric-loom on unobfuscated versions (26.1+) automatically.
    id("dev.kikugie.loom-back-compat")
}

version = "${property("mod.version")}+${sc.current.version}"
group = property("mod.group") as String
base.archivesName = property("mod.id") as String

val requiredJava: JavaVersion = when {
    sc.current.parsed >= "26.1" -> JavaVersion.VERSION_25
    else -> JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
    maven("https://repo.hypixel.net/repository/Hypixel/") { name = "hypixel" }
    // Auto updater
    maven("https://repo.nea.moe/releases") { name = "Autoupdater" }
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com/releases/")
}

val fabricApiVersion: String = sc.properties["deps.fabric_api"]
val clothConfigVersion: String = sc.properties["deps.cloth_config"]
val modmenuVersion: String = sc.properties["deps.modmenu"]
val mcCompat: String = sc.properties["mod.mc_compat"]

dependencies {
    // To change the versions see stonecutter.properties.toml
    minecraft("com.mojang:minecraft:${sc.current.version}")
    // Applies Mojang mappings on obfuscated versions, no-ops on unobfuscated ones.
    loomx.applyMojangMappings()

    // `mod*` dependency types work uniformly on 1.21.11 and 26.1+ - loom-back-compat converts them.
    modImplementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")

    implementation("net.hypixel:mod-api:1.0.1")
    include(modImplementation("moe.nea:libautoupdate:1.3.1") {
        exclude(group = "com.google.code.gson", module = "gson")
    }!!)
    include(implementation("com.esotericsoftware:kryonet:2.22.0-RC1")!!)

    // Specified here to make loom include it, using shading is a pain since there are many libraries provided by fabric.
    include(implementation("com.esotericsoftware:jsonbeans:0.7")!!)
    include(implementation("com.esotericsoftware.kryo:kryo:2.24.0")!!)
    include(implementation("org.objenesis:objenesis:2.1")!!)
    include(implementation("com.esotericsoftware.minlog:minlog:1.2")!!)

    include(modImplementation("me.shedaniel.cloth:cloth-config-fabric:$clothConfigVersion")!!)
    include(modImplementation("com.terraformersmc:modmenu:$modmenuVersion")!!)

    // Lombok (to generate code at compile time)
    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")
}

tasks.processResources {
    val props = mapOf(
        "version" to project.version.toString(),
        "minecraft" to mcCompat,
        "java" to requiredJava.majorVersion
    )
    inputs.properties(props)

    filesMatching("fabric.mod.json") { expand(props) }
}

tasks.withType<JavaCompile>().configureEach {
    options.release = requiredJava.majorVersion.toInt()
}

java {
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present. If you remove this line, sources will not be generated.
    withSourcesJar()

    sourceCompatibility = requiredJava
    targetCompatibility = requiredJava
}

tasks.jar {
    val archivesName = base.archivesName.get()
    inputs.property("archivesName", archivesName)

    from("LICENSE") {
        rename { "${it}_$archivesName" }
    }
}

tasks.register<Copy>("buildAndCollect") {
    group = "build"
    description = "Builds the mod jar and copies it to build/libs/{minecraft version}/"

    from(loomx.modJar.flatMap { it.archiveFile }, loomx.modSourcesJar.flatMap { it.archiveFile })
    into(rootProject.layout.buildDirectory.file("libs/${sc.current.version}"))
}
