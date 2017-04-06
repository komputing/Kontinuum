package kontinuum

import kontinuum.model.WorkPackage


fun processWebHook(event: String, payload: String) {
    when (event) {
        "push" -> {
            val pushInfo = pushEventAdapter.fromJson(payload)
            println("processing push from " + pushInfo.pusher.name + " to " + pushInfo.repository.full_name + " commits:" + pushInfo.commits.size)

            pushInfo.head_commit?.let {
                WorkPackageProvider.packages.add(WorkPackage(project = pushInfo.repository.full_name, commitHash = it.id))
            }

        }
    }

}