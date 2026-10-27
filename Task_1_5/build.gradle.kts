plugins { java; jacoco; application }
repositories { mavenCentral() }
java { toolchain { languageVersion = JavaLanguageVersion.of(21) } }
application { mainClass = "org.example.Main" }
dependencies { testImplementation("org.junit.jupiter:junit-jupiter:5.11.4"); testRuntimeOnly("org.junit.platform:junit-platform-launcher") }
tasks.test { useJUnitPlatform(); finalizedBy(tasks.jacocoTestReport) }
tasks.jacocoTestReport { dependsOn(tasks.test); reports { xml.required = true; html.required = true } }
tasks.withType<JavaCompile>().configureEach { options.encoding = "UTF-8" }
tasks.withType<Javadoc>().configureEach { options.encoding = "UTF-8" }
tasks.withType<Test>().configureEach { defaultCharacterEncoding = "UTF-8" }
tasks.withType<JavaExec>().configureEach { defaultCharacterEncoding = "UTF-8" }
