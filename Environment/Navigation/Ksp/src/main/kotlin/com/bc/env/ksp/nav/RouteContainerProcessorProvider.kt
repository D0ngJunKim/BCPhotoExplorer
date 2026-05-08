package com.bc.env.ksp.nav

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.validate

class RouteContainerProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return RouteContainerProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger,
            options = environment.options
        )
    }
}

private class RouteContainerProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {
    private var generated = false

    override fun process(resolver: Resolver): List<KSClassDeclaration> {
        if (generated) return emptyList()

        val symbolsByContainer = routeContainerSpecs.associateWith { spec ->
            resolver
                .getSymbolsWithAnnotation(spec.annotationName)
                .filterIsInstance<KSClassDeclaration>()
                .toList()
        }

        val invalidSymbols = symbolsByContainer.values
            .flatten()
            .distinct()
            .filterNot { it.validate() }
        if (invalidSymbols.isNotEmpty()) return invalidSymbols

        symbolsByContainer.forEach { (spec, symbols) ->
            generateContainerRegistry(spec, symbols)
        }

        generated = true
        return emptyList()
    }

    private fun generateContainerRegistry(
        spec: RouteContainerSpec,
        symbols: List<KSClassDeclaration>
    ) {
        val routes = symbols
            .filter { it.isRoute() }
            .sortedBy { it.qualifiedName?.asString().orEmpty() }

        if (symbols.size != routes.size) {
            symbols
                .filterNot { it.isRoute() }
                .forEach {
                    logger.error("@${spec.simpleName} can only be used on IRoute implementations.", it)
                }
        }

        val startRoutes = routes.filter { it.isStartRoute(spec.annotationName) }
        if (startRoutes.size > 1) {
            logger.error("Only one @${spec.simpleName}(start = true) route is allowed per module.", startRoutes[1])
            return
        }

        writeRegistry(
            packageName = options[spec.packageOption].orEmpty().ifBlank { spec.defaultPackage },
            objectName = options[spec.objectOption].orEmpty().ifBlank { spec.defaultObject },
            routes = routes,
            startRoute = startRoutes.firstOrNull() ?: routes.firstOrNull(),
            originatingFiles = routes.mapNotNull { it.containingFile }.distinct()
        )
    }

    private fun KSClassDeclaration.isRoute(): Boolean {
        return isAssignableTo(ROUTE_TYPE)
    }

    private fun KSClassDeclaration.isAssignableTo(qualifiedName: String): Boolean {
        val currentName = this.qualifiedName?.asString()
        if (currentName == qualifiedName) return true
        return superTypes.any { it.resolve().isAssignableTo(qualifiedName) }
    }

    private fun KSType.isAssignableTo(qualifiedName: String): Boolean {
        val declaration = declaration as? KSClassDeclaration ?: return false
        if (declaration.qualifiedName?.asString() == qualifiedName) return true
        return declaration.superTypes.any { it.resolve().isAssignableTo(qualifiedName) }
    }

    private fun KSClassDeclaration.isStartRoute(annotationName: String): Boolean {
        val annotation = annotations.firstOrNull {
            it.annotationType.resolve().declaration.qualifiedName?.asString() == annotationName
        } ?: return false
        return annotation.arguments.firstOrNull { it.name?.asString() == START_ARGUMENT }?.value as? Boolean ?: false
    }

    private fun writeRegistry(
        packageName: String,
        objectName: String,
        routes: List<KSClassDeclaration>,
        startRoute: KSClassDeclaration?,
        originatingFiles: List<KSFile>
    ) {
        val file = codeGenerator.createNewFile(
            dependencies = Dependencies(aggregating = true, sources = originatingFiles.toTypedArray()),
            packageName = packageName,
            fileName = objectName
        )

        file.bufferedWriter().use { writer ->
            writer.appendLine("package $packageName")
            writer.appendLine()
            writer.appendLine("import com.bc.env.nav.GeneratedRouteRegistry")
            writer.appendLine("import com.bc.env.nav.IRoute")
            writer.appendLine("import kotlin.reflect.KClass")
            writer.appendLine()
            writer.appendLine("public object $objectName : GeneratedRouteRegistry {")
            writer.appendLine("    override val routes: List<KClass<out IRoute>> = listOf(")
            if (routes.isEmpty()) {
                writer.appendLine("        com.bc.env.nav.EmptyRoute::class,")
            } else {
                routes.forEach { route ->
                    writer.appendLine("        ${route.qualifiedName!!.asString()}::class,")
                }
            }
            writer.appendLine("    )")
            writer.appendLine()
            val startRouteName = startRoute?.qualifiedName?.asString() ?: "com.bc.env.nav.EmptyRoute"
            writer.appendLine("    override val startRoute: KClass<out IRoute> = $startRouteName::class")
            writer.appendLine("}")
        }
    }

    private companion object {
        const val ROUTE_TYPE = "com.bc.env.nav.IRoute"
        const val START_ARGUMENT = "start"

        val routeContainerSpecs = listOf(
            RouteContainerSpec(
                simpleName = "MainContainer",
                annotationName = "com.bc.env.nav.annotation.MainContainer",
                packageOption = "mainContainer.package",
                objectOption = "mainContainer.object",
                defaultPackage = "com.bc.generated.nav",
                defaultObject = "MainContainerRoutes"
            ),
            RouteContainerSpec(
                simpleName = "OverlayContainer",
                annotationName = "com.bc.env.nav.annotation.OverlayContainer",
                packageOption = "overlayContainer.package",
                objectOption = "overlayContainer.object",
                defaultPackage = "com.bc.generated.nav",
                defaultObject = "OverlayContainerRoutes"
            )
        )
    }
}

private data class RouteContainerSpec(
    val simpleName: String,
    val annotationName: String,
    val packageOption: String,
    val objectOption: String,
    val defaultPackage: String,
    val defaultObject: String
)
