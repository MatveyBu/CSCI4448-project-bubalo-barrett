# CSCI4448-project-bubalo-barett

## Package Structure

### `spotifyanalytics.domain`
The raw data of the application. These classes represent real world things — artists, songs, plays.
They have no logic, no dependencies on anything else in the project, and no opinion about what you
do with them. Everything else in the project depends on these. Think of it as the **vocabulary** of the app.

### `spotifyanalytics.metrics`
The intelligence of the application. This is where the Strategy pattern lives — the interface defines
what a metric is, and each implementation defines how to compute one. This package takes spotifyanalytics.domain data
as input and produces results as output. It knows about `spotifyanalytics.domain` but nothing else.

### `spotifyanalytics.service`
The coordination layer. Classes here don't compute or store anything themselves — they wire other
pieces together and orchestrate the work. `DashboardService` sits here because its only job is to
run spotifyanalytics.metrics against history and collect results. It knows about both `spotifyanalytics.domain` and `spotifyanalytics.metrics` but has
no logic of its own.

## Dependency Flow
```
spotifyanalytics.domain      ← knows about nothing else
  ↑
spotifyanalytics.metrics     ← knows about spotifyanalytics.domain
  ↑
spotifyanalytics.service     ← knows about spotifyanalytics.domain and spotifyanalytics.metrics
```

Each layer only looks downward. `spotifyanalytics.service` is never imported by `spotifyanalytics.metrics`, and `spotifyanalytics.metrics` is never
imported by `spotifyanalytics.domain`. That one-way dependency flow is what keeps the design clean and testable.