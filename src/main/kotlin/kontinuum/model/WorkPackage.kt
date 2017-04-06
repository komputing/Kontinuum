package kontinuum.model

data class WorkPackage(val project: String,
                       val commitHash: String,
                       var workPackageStatus: WorkPackageStatus,
                       var stageInfoList: MutableList<StageInfo> = mutableListOf())
