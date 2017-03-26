Kontinuum

Continuous integration in Kotlin for ([ligi's](http://ligi.de)) Android apps

This is an attempt to replace my current Jenkins setup - this is an experiment and not (yet or perhaps even ever) intended to be used by others. Some goals of this endeavour:

 * Publish reports (tests,lint,logs,..) & artifacts (apks,aar,jar,,,) to content addressable storage [IPFS](http://ipfs.io) (perhaps later on also ethereum's swarm)
 * Link to the results via content-hash as github status updates on the commits
 * Github-webhook with little attack surface (currently I am hesitant to expose jenkins to the internet and so constantly run into github quota issues as I am polling)
 * Act as Github integration: https://developer.github.com/early-access/integrations
 * Abort build by dereferencing the commit (e.g. via force-push)
 * Backfilling (build more flavors and commits between pushes when idle e.g. sleeping)
 * Intelligent stage ordering (e.g. if there is a lint-failure - the lint stage is next when backfilling or user push)
 * API for status-monitoring
 * Create artifacts for tags
 * Uninstalling the apps before running UI-tests


Credits:

 - Icon: https://openclipart.org/detail/219802/mikey-rat-copywrong
 