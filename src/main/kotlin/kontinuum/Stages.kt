package kontinuum

import kontinuum.model.StageInfo
import kontinuum.model.StageStatus
import kontinuum.model.WorkPackage
import kontinuum.model.github.GithubCommitState.error
import kontinuum.model.github.GithubCommitState.success
import java.io.File

fun executeStageByName(stage: String, currentWorkPackage: WorkPackage, toPath: File, stageInfo: StageInfo) {

    when (stage) {
        "spoon" -> executeGradle(currentWorkPackage, stageInfo, toPath, "spoon -PsingleFlavor", { outPath, _ ->
            toPath.walk().filter { it.name == "spoon" }.forEach { it.copyRecursively(File(outPath, it.name), true) }
        })

        "lint" -> executeGradle(currentWorkPackage, stageInfo, toPath, "lint -PsingleFlavor", { outPath, _ ->
            toPath.walk().filter { it.name.startsWith("lint-results") }.forEach { it.copyTo(File(outPath, it.name), true) }
        })

        "test" -> executeGradle(currentWorkPackage, stageInfo, toPath, "test -PsingleFlavor", { outPath, _ ->
            toPath.walk().filter { it.name == "tests" }.forEach { it.copyRecursively(File(outPath, it.name), true) }
        })

        "assemble" -> executeGradle(currentWorkPackage, stageInfo, toPath, "assembleRelease", { outPath, _ ->
            copyAPKandAAR(toPath, outPath)
        })

        "assembleDebug" -> executeGradle(currentWorkPackage, stageInfo, toPath, "assembleDebug", { outPath, _ ->
            copyAPKandAAR(toPath, outPath)
        })

    // this stage is not for android - it is for kotlinkci - TODO fail if used wrongly
        "build" -> executeGradle(currentWorkPackage, stageInfo, toPath, "build", { outPath, _ ->
            toPath.walk().filter { it.name == "distributions" }.forEach { it.copyRecursively(File(outPath, it.name), true) }
        })

        "run" -> executeGradle(currentWorkPackage, stageInfo, toPath, "run", { outPath, _ ->
            toPath.walk().filter {
                println("found " + it.name)
                it.name == "output" }.forEach { it.copyRecursively(File(outPath, it.name), true) }
        })
    }
}

private fun copyAPKandAAR(toPath: File, outPath: File) {
    toPath.walk().filter { it.name.endsWith(".apk") || it.name.endsWith(".aar") }.forEach {
        it.copyRecursively(File(outPath, it.name), true)
    }
}

fun executeGradle(currentWorkPackage: WorkPackage, stageInfo: StageInfo, toPath: File, commandAndParams: String, postProcess: (outPath: File, result: Int) -> Unit) {
    doIn(stageInfo, currentWorkPackage, { outPath ->
        val result = executeAndPrint("./gradlew $commandAndParams -PisCI", workPath = toPath, outPath = outPath)
        postProcess(outPath, result)
        stageInfo.status = if (result == 0) StageStatus.SUCCESS else StageStatus.ERROR
        if (result == 0) success else error
    })
}