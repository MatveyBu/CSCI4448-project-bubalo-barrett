# CSCI4448-project-bubalo-barett

## Package Structure

### `domain`
The raw data of the application. These classes represent real world things — artists, songs, plays.
They have no logic, no dependencies on anything else in the project, and no opinion about what you
do with them. Everything else in the project depends on these. Think of it as the **vocabulary** of the app.

### `metrics`
The intelligence of the application. This is where the Strategy pattern lives — the interface defines
what a metric is, and each implementation defines how to compute one. This package takes domain data
as input and produces results as output. It knows about `domain` but nothing else.

### `service`
The coordination layer. Classes here don't compute or store anything themselves — they wire other
pieces together and orchestrate the work. `DashboardService` sits here because its only job is to
run metrics against history and collect results. It knows about both `domain` and `metrics` but has
no logic of its own.

## Dependency Flow
```
domain      ← knows about nothing else
  ↑
metrics     ← knows about domain
  ↑
service     ← knows about domain and metrics
```

Each layer only looks downward. `service` is never imported by `metrics`, and `metrics` is never
imported by `domain`. That one-way dependency flow is what keeps the design clean and testable.