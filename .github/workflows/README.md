# CI/CD workflows

## Convention and Standard
### Naming
- Each workflow should have a unique name and filename.
    - Name the **main** workflows as `workflow-filename.yml`
    - Name the **reusable** workflows as `_reusable-workflow.yml` (with `_` prefix)
- A workflow’s name should **not** be changed once it has been committed to the repository, as this may cause history mismatches and unexpected errors. 
Therefore, it is recommended to consider changes carefully before modifying existing workflows.

### Directory structure
- `.github/workflows`: All **runnable workflows** must be placed in this directory, as required by the official GitHub Actions documentation. Otherwise, they may not execute correctly.
- `.github/scripts`: Place **reusable or lengthy bash scripts** in this directory so they can be referenced and reused in workflows.

### Workflows (Planned)
- Changes on `feature` branch: Run unit tests, linting, security check, etc
- PR on `develop` branch (for integration/staging):
    - Unit + Integration tests (API, DB, UI, etc)
    - Build and push staging images
    - Deploy to staging environment and test (optional)
- PR on `main` branch (for production): Final build verification
- Completed merge on `main` branch (for production):
    - Build and push production-ready images with version tags
    - Deploy to production environment

## Reference
### GitHub Actions Workflows
- Official documentation: [GitHub Actions Docs - How To](https://docs.github.com/en/actions/how-tos)
- Advanced customization: [Mert Mengü - Guide to GitHub Actions for Advanced CI/CD Workflows](https://medium.com/@mertmengu/guide-to-github-actions-for-advanced-ci-cd-workflows-1e494271ac22)
- GitHub Actions with Spring Boot: [Pudding Entertainment - GitHub Actions: Spring Boot Application Build and Deployment](https://pudding-entertainment.medium.com/github-actions-spring-boot-application-build-and-deployment-c52a2a233cb9)

### Branching Strategy
- Different branching strategies, including using develop and main branch: [Devtron - Branching Strategy for CI/CD](https://devtron.ai/blog/best-branching-strategy-for-ci-cd/)
- Run tests on feature and master branch: [TeamCity - Branching Strategy for CI/CD](https://www.jetbrains.com/teamcity/ci-cd-guide/concepts/branching-strategy/)