package kontinuum

import kontinuum.model.WorkPackage

object WorkPackageProvider {

    val packages = mutableListOf<WorkPackage>()

    fun getSortedAndCleaned() =
        packages.sortedBy { it.epochSeconds }.associateBy { it.project + "/" + it.branch }.values
                    .sortedByDescending { it.epochSeconds  }

}