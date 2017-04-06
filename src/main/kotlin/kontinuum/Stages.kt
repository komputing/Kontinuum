package kontinuum

import kontinuum.model.WorkPackage
import kontinuum.model.github.GithubCommitState.error
import kontinuum.model.github.GithubCommitState.success
import java.io.File

fun executeStageByName(stage: String, currentWorkPackage: WorkPackage, toPath: File) {

    when (stage) {
        "spoon" -> executeGradle(stage, currentWorkPackage, toPath, "spoon -PsingleFlavor", { outPath,_ ->
            toPath.walk().filter { it.name == "spoon" }.forEach { it.copyRecursively(File(outPath, it.name), true) }
        })

        "lint" -> executeGradle(stage, currentWorkPackage, toPath, "lint -PsingleFlavor", { outPath,_ ->
            toPath.walk().filter { it.name.startsWith("lint-results") }.forEach { it.copyTo(File(outPath, it.name), true) }
        })

        "test" -> executeGradle(stage, currentWorkPackage, toPath, "test -PsingleFlavor", { outPath,_ ->
            toPath.walk().filter { it.name == "tests" }.forEach { it.copyRecursively(File(outPath, it.name), true) }
        })

        "assemble" -> executeGradle(stage, currentWorkPackage, toPath, "assembleRelease", { outPath,_ ->
            toPath.walk().filter { it.name.endsWith(".apk") || it.name.endsWith(".aar") }.forEach { it.copyRecursively(File(outPath, it.name), true) }
        })

        // this stage is not for android - it is for kotlinkci - TODO fail if used wrongly
        "build" -> executeGradle(stage, currentWorkPackage, toPath, "build", { outPath,_ ->
            toPath.walk().filter { it.name == "distributions" }.forEach { it.copyRecursively(File(outPath, it.name), true) }
        })
    }
}

fun executeGradle(stage: String, currentWorkPackage: WorkPackage, toPath: File, commandAndParams: String, postProcess: (outPath: File, result: Int) -> Unit) {
    doIn(stage, currentWorkPackage, { outPath ->
        val result = executeAndPrint("./gradlew $commandAndParams", workPath = toPath, outPath = outPath)
        postProcess(outPath, result)
        if (result == 0) success else error
    })
}