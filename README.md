# Blood Point — Android Blood-Donor App

Android coursework based on the original **[imShakil/BloodBank](https://github.com/imShakil/BloodBank)** project. The existing GPL-3.0 license and original attribution remain in place. This repository is not presented as wholly original authorship.

## Source branch

The application code is on **`master`**. The old `main` branch contained documentation rather than the full Android app.

## Features represented in source

User login, donor profiles and search, blood requests, Firebase integration and location-related UI.

## Explore locally

```bash
git clone --branch master https://github.com/takitamim/BloodBank_CSE2100.git
```

Open the project in Android Studio. It uses a legacy Android SDK 28 / support-library stack; a compatible Gradle/JDK environment or migration may be required. Configure your own Firebase project and restricted Maps key before using the integration. Do not assume bundled service configuration is available or appropriate for new deployments.

## Source map

- `app/src/main/java/`: activities, adapters and application logic.
- `app/src/main/res/`: Android resources.
- `app/build.gradle`: application build configuration.
- `LICENSE`: original GPL-3.0 license.

Related coursework copy: [BloodBank_CSE2100](https://github.com/takitamim/BloodBank_CSE2100). Both repositories are retained to preserve history and existing links. No Android build was executed during documentation cleanup.
