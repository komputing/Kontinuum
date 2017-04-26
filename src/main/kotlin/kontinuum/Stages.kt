package kontinuum

import kontinuum.model.StageInfo
import kontinuum.model.StageStatus
import kontinuum.model.WorkPackage
import kontinuum.model.github.GithubCommitState.error
import kontinuum.model.github.GithubCommitState.success
import java.io.File

fun executeStageByName(stage: String, currentWorkPackage: WorkPackage, toPath: File, stageInfo: StageInfo) {

    when (stage) {
        "spoon" -> executeGradle(stage, currentWorkPackage, stageInfo, toPath, "spoon -PsingleFlavor", { outPath, _ ->
            toPath.walk().filter { it.name == "spoon" }.forEach { it.copyRecursively(File(outPath, it.name), true) }
        })

        "lint" -> executeGradle(stage, currentWorkPackage, stageInfo, toPath, "lint -PsingleFlavor", { outPath, _ ->
            toPath.walk().filter { it.name.startsWith("lint-results") }.forEach { it.copyTo(File(outPath, it.name), true) }
        })

        "test" -> executeGradle(stage, currentWorkPackage, stageInfo, toPath, "test -PsingleFlavor", { outPath, _ ->
            toPath.walk().filter { it.name == "tests" }.forEach { it.copyRecursively(File(outPath, it.name), true) }
        })

        "assemble" -> executeGradle(stage, currentWorkPackage, stageInfo, toPath, "assembleRelease", { outPath, _ ->
            toPath.walk().filter { it.name.endsWith(".apk") || it.name.endsWith(".aar") }.forEach {
                it.copyRecursively(File(outPath, it.name), true)
            }
        })

        // this stage is not for android - it is for kotlinkci - TODO fail if used wrongly
        "build" -> executeGradle(stage, currentWorkPackage, stageInfo, toPath, "build", { outPath, _ ->
            toPath.walk().filter { it.name == "distributions" }.forEach { it.copyRecursively(File(outPath, it.name), true) }
        })
    }
}

fun executeGradle(stage: String, currentWorkPackage: WorkPackage, stageInfo: StageInfo, toPath: File, commandAndParams: String, postProcess: (outPath: File, result: Int) -> Unit) {
    doIn(stage, currentWorkPackage, { outPath ->
        val result = executeAndPrint("./gradlew $commandAndParams -PisCI", workPath = toPath, outPath = outPath)
        postProcess(outPath, result)
        stageInfo.status = if (result == 0) StageStatus.SUCCESS else StageStatus.ERROR
        if (result == 0) success else error
    })
}