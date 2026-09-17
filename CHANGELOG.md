# Changelog

## [2.1.0](https://github.com/philips-software/cerberus/compare/v2.0.1...v2.1.0) (2026-09-17)


### Features

* **cpd:** detect duplicates in C# sources ([17fba5c](https://github.com/philips-software/cerberus/commit/17fba5cb6cc81d91335ac00408fa683ac47a0079))


### Bug Fixes

* **cpd:** honour --language instead of tokenizing everything as Java ([4259232](https://github.com/philips-software/cerberus/commit/4259232e645595c475f66a5c5886a337f08a290e))
* **rulesets:** reference PMD's bestpractices category ([9aa4e98](https://github.com/philips-software/cerberus/commit/9aa4e98304e4aa5352586e278f5ceb5297cdf973))

## [2.0.1](https://github.com/philips-software/cerberus/compare/v2.0.0...v2.0.1) (2026-02-08)


### Bug Fixes

* **pitest:** resolve Spring plugin warning and adjust mutation threshold ([fdb7fc3](https://github.com/philips-software/cerberus/commit/fdb7fc3f586c6a1b81dbd6c5b570fb86ceb895ca))

## [2.0.0](https://github.com/philips-software/cerberus/compare/v1.0.0...v2.0.0) (2026-02-08)


### ⚠ BREAKING CHANGES

* PMD downgrade from 7.0.0 to 6.55.0 changes CPD API

### Features

* Gradle 8.7 compatibility with Java 17
* Updated dependencies for modern Jakarta EE standards
* Enhanced code metrics with CK library 0.7.0

### Bug Fixes

* handle NaN values in JSON serialization ([0daf977](https://github.com/philips-software/cerberus/commit/0daf977))

### Code Refactoring

* adapt CPD for PMD 6.x API changes ([58359f4](https://github.com/philips-software/cerberus/commit/58359f4))
* migrate from javax.validation to jakarta.validation ([9b53f45](https://github.com/philips-software/cerberus/commit/9b53f45))
* update for CK library 0.7.0 API changes ([b7ac07a](https://github.com/philips-software/cerberus/commit/b7ac07a))

### Build System

* update dependencies for Gradle 8.7 compatibility ([696710e](https://github.com/philips-software/cerberus/commit/696710e))

### Tests

* update LOC assertions for CK 0.7.0 line counting ([48332d9](https://github.com/philips-software/cerberus/commit/48332d9))

### Miscellaneous

* add asdf tool version configuration ([57e95ea](https://github.com/philips-software/cerberus/commit/57e95ea))
* add C# build artifacts to gitignore ([1b59dce](https://github.com/philips-software/cerberus/commit/1b59dce))
* add Visual Studio solution for C# test resources ([e449feb](https://github.com/philips-software/cerberus/commit/e449feb))
* update quality gates for Gradle 8.7 and Spotbugs ([108e715](https://github.com/philips-software/cerberus/commit/108e715))
* upgrade Gradle wrapper to 8.7 ([4b79239](https://github.com/philips-software/cerberus/commit/4b79239))
