# Release Process

Follow these steps to publish a new release:

  1. Update [the release notes](../CHANGELOG.md).

  2. Set and tag the release version:
     ```
     mvn versions:set --define newVersion=<release-version>
     mvn versions:commit
     git commit --all --message=<release-version>
     git tag <release-version>
     git push --tags
     ```

  3. Deploy the release to Maven Central Repository:
     ```
     mvn --activate-profiles release clean deploy
     ```

  4. Set the next snapshot version:
     ```
     mvn versions:set --define newVersion=<next-snapshot-version>
     mvn versions:commit
     git commit --all --message=<next-snapshot-version>
     git push
     ```

  5. [Add the release][GitHub] on GitHub.
    
     [GitHub]: https://github.com/jvirtanen/tagline/releases/new
