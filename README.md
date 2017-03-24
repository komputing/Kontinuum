Kontinuum

Continuous integration in Kotlin for ([ligi's](http://ligi.de)) Android apps

This is an attempt to replace my current Jenkins setup - this is an experiment and not (yet or perhaps even ever) intended to be used by others. Some goals of this endeavour:

 * publish reports (tests,lint,logs,..) & artifacts (apks,aar,jar,,,) to content addressable storage [IPFS](http://ipfs.io) (perhaps later on also ethereum's swarm)
 * link to the results via content-hash as github status updates on the commits
 * github-webhook with little attack surface (currently I am hesitant to expose jenkins to the internet and so constantly run into github quota issues as I am polling)
 * abort build by dereferencing the commit (e.g. via force-push)
 * backfilling (build more flavors and commits between pushes when idle e.g. sleeping)
 * API for status-monitoring
 * Uninstalling the apps before running UI-tests
