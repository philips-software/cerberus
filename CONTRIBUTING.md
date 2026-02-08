# Contributing

This project welcomes contributions and suggestions. 
When you submit a pull request, we have checks which run on your PR for standards that we expect.

## Prerequisites

Before contributing, ensure you have the following installed:

1. **asdf** - Tool version manager
   - Installation guide: https://asdf-vm.com/guide/getting-started.html
   - Install required plugins:
     ```bash
     asdf plugin add java
     asdf plugin add gradle
     ```

2. **Install project tools**
   - Navigate to the project directory and run:
     ```bash
     asdf install
     ```
   - This will install the Java and Gradle versions specified in `.tool-versions`

3. **Verify installation**
   ```bash
   java -version
   gradle -version
   ```

## Code of Conduct

This project has adopted the [Philips Open Source Code Of Conduct](https://github.com/philips-software/philips-howto-open-source).

## How to contribute

- File or vote up issues
- Improve documentation
- Fix bugs or add features

### Intro to Git and GitHub

When contributing to documentation or code changes, you'll need to have a GitHub account and a basic understanding of Git.
Check out the links below to get started.

- Make sure you have a [GitHub account][github-signup].
- GitHub Help:
  - [Git and GitHub learning resources][learn-git].
  - [GitHub Flow Guide][github-flow].
  - [Fork a repo][github-fork].
  - [About Pull Requests][github-pr].

## Contributing to issues

- Check if the issue you are going to file already exists in our GitHub [issues].
- If you do not see your problem captured, please file a new issue and follow the provided template.
- If the an open issue exists for the problem you are experiencing, vote up the issue or add a comment.

## Contributing to code

- Before writing a fix or feature enhancement, ensure that an issue is logged.
- Be prepared to discuss a feature and take feedback.
- Include unit tests and updates documentation to complement the change.

### Development workflow

1. **Set up your environment**
   ```bash
   asdf install  # Install Java and Gradle from .tool-versions
   ```

2. **Run quality checks locally**
   ```bash
   ./gradlew clean build pitest shadowJar
   ```
   This runs all quality gates including tests, PMD, CPD, Checkstyle, code coverage, and mutation testing.

3. **Make your changes**
   - Follow the existing code style and conventions
   - Ensure all quality checks pass before submitting a PR

4. **Submit a pull request**
   - Target the `main` branch
   - Ensure CI workflows pass on your PR
   - Address any review feedback promptly

[learn-git]: https://help.github.com/en/articles/git-and-github-learning-resources
[github-flow]: https://guides.github.com/introduction/flow/
[github-signup]: https://github.com/signup/free
[github-fork]: https://help.github.com/en/github/getting-started-with-github/fork-a-repo
[github-pr]: https://help.github.com/en/github/collaborating-with-issues-and-pull-requests/about-pull-requests
[github-pr-create]: https://help.github.com/en/github/collaborating-with-issues-and-pull-requests/creating-a-pull-request-from-a-fork
[build]: docs/scenarios/install-instructions.md#building-from-source