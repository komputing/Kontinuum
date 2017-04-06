package kontinuum

import kontinuum.model.WorkPackage
import kontinuum.model.github.GithubCommitState.error
import kontinuum.model.github.GithubCommitState.success
import java.io.File

fun executeStageByName(stage: String, currentWorkPackage: WorkPackage, toPath: File) {

    when (stage) {
        "spoon" -> doIn(stage, currentWorkPackage, { outPath ->
            val spoonResult = executeAndPrint("./gradlew", "clean", "spoon", "-PsingleFlavor", workPath = toPath, outPath = outPath)
            println("finished spoon with " + spoonResult)
            toPath.walk().filter { it.name == "spoon" }.forEach { it.copyRecursively(File(outPath, it.name), true) }

            if (spoonResult == 0) success else error
        })

        "lint" -> doIn(stage, currentWorkPackage, { outPath ->

            val result = executeAndPrint("./gradlew", "lint", "-PsingleFlavor", workPath = toPath, outPath = outPath)

            if (result == 0) {
                toPath.walk().filter { it.name.startsWith("lint-results") }.forEach { it.copyTo(File(outPath, it.name), true) }
                success
            } else {
                error
            }
        })

        "test" -> doIn(stage, currentWorkPackage, { outPath ->

            val result = executeAndPrint("./gradlew", "test", "-PsingleFlavor", workPath = toPath, outPath = outPath)

            if (result == 0) {
                toPath.walk().filter { it.name == "tests" }.forEach { it.copyRecursively(File(outPath, it.name), true) }
                success
            } else {
                error
            }
        })

        "assemble" -> doIn(stage, currentWorkPackage, { outPath ->

            val result = executeAndPrint("./gradlew", "assembleRelease", workPath = toPath, outPath = outPath)

            if (result == 0) {
                toPath.walk().filter { it.name.endsWith(".apk") || it.name.endsWith(".aar") }.forEach { it.copyRecursively(File(outPath, it.name), true) }
                success
            } else {
                error
            }
        })

        // this stage is not for android - it is for kotlinkci - TODO fail if used wrongly
        "build" -> doIn(stage, currentWorkPackage, { outPath ->

            val result = executeAndPrint("./gradlew", "build", workPath = toPath, outPath = outPath)

            if (result == 0) {
                toPath.walk().filter { it.name == "distributions" }.forEach { it.copyRecursively(File(outPath, it.name), true) }
                success
            } else {
                error
            }
        })

    }

}
