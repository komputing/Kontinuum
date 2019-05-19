package kontinuum

import kontinuum.model.WorkPackage

object WorkPackageProvider {

    val packages = mutableListOf<WorkPackage>()

    fun getSortedAndCleaned() =
            packages.sortedBy { it.epochSeconds }
                    .map{ it.project+"/"+it.branch to it }.toMap().values
                    .sortedByDescending { it.epochSeconds  }

}