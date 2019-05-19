import kontinuum.WorkPackageProvider
import kontinuum.model.WorkPackage
import kontinuum.model.WorkPackageStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class TheWorkPackageProvider {

    @Test
    fun getSortedAndCleanedShouldWork() {

        val olderPackage = WorkPackage("prj", "commitHash", 1L, branch = "branch1", installationId = "installationId", workPackageStatus = WorkPackageStatus.PENDING)
        WorkPackageProvider.packages.add(olderPackage)

        val otherPackage = WorkPackage("prj", "commitHash", 2L, branch = "branch2", installationId = "installationId", workPackageStatus = WorkPackageStatus.PENDING)
        WorkPackageProvider.packages.add(otherPackage)

        val newerPackage = WorkPackage("prj", "commitHash", 3L, branch = "branch1", installationId = "installationId", workPackageStatus = WorkPackageStatus.PENDING)
        WorkPackageProvider.packages.add(newerPackage)

        assertThat(WorkPackageProvider.getSortedAndCleaned()).containsExactly(newerPackage, otherPackage)
    }
}