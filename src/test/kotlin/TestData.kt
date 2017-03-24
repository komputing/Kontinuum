

val GIT_PUSH_WEBHOOK_BODY = """
{
  "ref": "refs/heads/master",
  "before": "a41929413b7e18616043f94bd2eec37aa48ba18d",
  "after": "6c527d796c68151a75d46b129a2d0f22fbd78008",
  "created": false,
  "deleted": false,
  "forced": false,
  "base_ref": null,
  "compare": "https://github.com/ligi/GithubCommitStatusTest/compare/a41929413b7e...6c527d796c68",
  "commits": [
    {
      "id": "6c527d796c68151a75d46b129a2d0f22fbd78008",
      "tree_id": "72d6f073d781d36115139a278dc869e54420ee4c",
      "distinct": true,
      "message": "FOO",
      "timestamp": "2017-03-25T16:40:21+01:00",
      "url": "https://github.com/ligi/GithubCommitStatusTest/commit/6c527d796c68151a75d46b129a2d0f22fbd78008",
      "author": {
        "name": "ligi",
        "email": "ligi@ligi.de",
        "username": "ligi"
      },
      "committer": {
        "name": "ligi",
        "email": "ligi@ligi.de",
        "username": "ligi"
      },
      "added": [

      ],
      "removed": [

      ],
      "modified": [
        "README.MD"
      ]
    }
  ],
  "head_commit": {
    "id": "6c527d796c68151a75d46b129a2d0f22fbd78008",
    "tree_id": "72d6f073d781d36115139a278dc869e54420ee4c",
    "distinct": true,
    "message": "FOO",
    "timestamp": "2017-03-25T16:40:21+01:00",
    "url": "https://github.com/ligi/GithubCommitStatusTest/commit/6c527d796c68151a75d46b129a2d0f22fbd78008",
    "author": {
      "name": "ligi",
      "email": "ligi@ligi.de",
      "username": "ligi"
    },
    "committer": {
      "name": "ligi",
      "email": "ligi@ligi.de",
      "username": "ligi"
    },
    "added": [

    ],
    "removed": [

    ],
    "modified": [
      "README.MD"
    ]
  },
  "repository": {
    "id": 85476264,
    "name": "GithubCommitStatusTest",
    "full_name": "ligi/GithubCommitStatusTest",
    "owner": {
      "name": "ligi",
      "email": "ligi@ligi.de",
      "login": "ligi",
      "id": 111600,
      "avatar_url": "https://avatars1.githubusercontent.com/u/111600?v=3",
      "gravatar_id": "",
      "url": "https://api.github.com/users/ligi",
      "html_url": "https://github.com/ligi",
      "followers_url": "https://api.github.com/users/ligi/followers",
      "following_url": "https://api.github.com/users/ligi/following{/other_user}",
      "gists_url": "https://api.github.com/users/ligi/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/ligi/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/ligi/subscriptions",
      "organizations_url": "https://api.github.com/users/ligi/orgs",
      "repos_url": "https://api.github.com/users/ligi/repos",
      "events_url": "https://api.github.com/users/ligi/events{/privacy}",
      "received_events_url": "https://api.github.com/users/ligi/received_events",
      "type": "User",
      "site_admin": false
    },
    "private": false,
    "html_url": "https://github.com/ligi/GithubCommitStatusTest",
    "description": null,
    "fork": false,
    "url": "https://github.com/ligi/GithubCommitStatusTest",
    "forks_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/forks",
    "keys_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/keys{/key_id}",
    "collaborators_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/collaborators{/collaborator}",
    "teams_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/teams",
    "hooks_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/hooks",
    "issue_events_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/issues/events{/number}",
    "events_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/events",
    "assignees_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/assignees{/user}",
    "branches_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/branches{/branch}",
    "tags_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/tags",
    "blobs_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/git/blobs{/sha}",
    "git_tags_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/git/tags{/sha}",
    "git_refs_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/git/refs{/sha}",
    "trees_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/git/trees{/sha}",
    "statuses_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/statuses/{sha}",
    "languages_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/languages",
    "stargazers_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/stargazers",
    "contributors_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/contributors",
    "subscribers_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/subscribers",
    "subscription_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/subscription",
    "commits_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/commits{/sha}",
    "git_commits_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/git/commits{/sha}",
    "comments_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/comments{/number}",
    "issue_comment_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/issues/comments{/number}",
    "contents_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/contents/{+path}",
    "compare_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/compare/{base}...{head}",
    "merges_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/merges",
    "archive_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/{archive_format}{/ref}",
    "downloads_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/downloads",
    "issues_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/issues{/number}",
    "pulls_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/pulls{/number}",
    "milestones_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/milestones{/number}",
    "notifications_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/notifications{?since,all,participating}",
    "labels_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/labels{/name}",
    "releases_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/releases{/id}",
    "deployments_url": "https://api.github.com/repos/ligi/GithubCommitStatusTest/deployments",
    "created_at": 1489928462,
    "updated_at": "2017-03-19T13:01:02Z",
    "pushed_at": 1490456430,
    "git_url": "git://github.com/ligi/GithubCommitStatusTest.git",
    "ssh_url": "git@github.com:ligi/GithubCommitStatusTest.git",
    "clone_url": "https://github.com/ligi/GithubCommitStatusTest.git",
    "svn_url": "https://github.com/ligi/GithubCommitStatusTest",
    "homepage": null,
    "size": 0,
    "stargazers_count": 0,
    "watchers_count": 0,
    "language": null,
    "has_issues": true,
    "has_downloads": true,
    "has_wiki": true,
    "has_pages": false,
    "forks_count": 0,
    "mirror_url": null,
    "open_issues_count": 0,
    "forks": 0,
    "open_issues": 0,
    "watchers": 0,
    "default_branch": "master",
    "stargazers": 0,
    "master_branch": "master"
  },
  "pusher": {
    "name": "ligi",
    "email": "ligi@ligi.de"
  },
  "sender": {
    "login": "ligi",
    "id": 111600,
    "avatar_url": "https://avatars1.githubusercontent.com/u/111600?v=3",
    "gravatar_id": "",
    "url": "https://api.github.com/users/ligi",
    "html_url": "https://github.com/ligi",
    "followers_url": "https://api.github.com/users/ligi/followers",
    "following_url": "https://api.github.com/users/ligi/following{/other_user}",
    "gists_url": "https://api.github.com/users/ligi/gists{/gist_id}",
    "starred_url": "https://api.github.com/users/ligi/starred{/owner}{/repo}",
    "subscriptions_url": "https://api.github.com/users/ligi/subscriptions",
    "organizations_url": "https://api.github.com/users/ligi/orgs",
    "repos_url": "https://api.github.com/users/ligi/repos",
    "events_url": "https://api.github.com/users/ligi/events{/privacy}",
    "received_events_url": "https://api.github.com/users/ligi/received_events",
    "type": "User",
    "site_admin": false
  },
  "installation": {
    "id": 17068
  }
}
    """