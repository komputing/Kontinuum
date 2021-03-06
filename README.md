Kontinuum

Continuous integration in Kotlin

This is an attempt to replace my current Jenkins setup - this is an experiment and not (yet or perhaps even ever) intended to be used by others. Some goals of this endeavour:

 * Publish reports (tests,lint,logs,..) & artifacts (apks,aar,jar,..) to content addressable storage [IPFS](http://ipfs.io) (perhaps later on also ethereum's swarm)
 * Link to the results via content-hash as github status updates on the commits
 * Github-webhook with little attack surface (currently I am hesitant to expose jenkins to the internet and so constantly run into github quota issues as I am polling)
 * Act as Github integration: https://developer.github.com/early-access/integrations
 * Abort build by dereferencing the commit (e.g. via force-push)
 * Backfilling (build more flavors and commits between pushes when idle e.g. sleeping)
 * Intelligent stage ordering (e.g. if there is a lint-failure - the lint stage is next when backfilling or user push)
 * API for status-monitoring
 * Create artifacts for tags
 * Uninstalling the apps before running UI-tests
 * (very far away) Use [truebit](https://medium.com/@chriseth/truebit-c8b6a129d580) and reproducable builds for verification (check if build-servers are producing the correct output or are e.g. compromized)


![](https://gateway.ipfs.io/ipfs/QmeBnMfiQAzGaxeFqgh72vXhboSTwphGtcD9B8kduY3ri9)
![](https://gateway.ipfs.io/ipfs/QmQFXM62kjm8oohou1VEU8JHRvqDHLus26hG95swHngM4C)

Credits:

 - Icon: https://openclipart.org/detail/219802/mikey-rat-copywrong
 