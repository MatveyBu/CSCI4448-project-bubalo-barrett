# Spotify Listening Analytics

A Java web application that connects to the Spotify API to import a user's listening
history and display analytics on a dashboard. The app tracks top artists, top tracks,
and total time listened, with data persisted in a SQL database to support trend analysis
over weeks and months.

## Design Patterns

### 1. Strategy Pattern
Used in the `metrics` package through the `ListeningMetric` interface. Each metric class
(`TopArtistsMetric`, `TopTracksMetric`, `TimeListenedMetric`) implements this interface
and provides its own `compute()` logic. `DashboardService` holds a list of `ListeningMetric`
objects and calls `compute()` on each without knowing the concrete type, allowing new
metrics to be added without changing existing code.

### 2. Factory Pattern
Used in `MetricFactory`, the single place responsible for constructing and assembling
the list of metrics. A `DEFAULT_LIMIT` constant centralizes configuration so nothing
outside this class needs to know which metrics exist or how to build them.

### 3. Facade Pattern
Used in the `spotifyanalytics.integration` package through the `SpotifyClient` interface,
which provides simple methods like `getRecentlyPlayed(int limit)` and
`getTopTracks(int limit)`. This abstraction will hide lower-level integration details
such as OAuth, HTTP requests, rate limiting, and JSON parsing from the rest of the
application. `FakeSpotifyClient` is the current implementation used for hardcoded
development/testing data, while a real API-backed client can be added later without
changing code that depends on the facade.


## OO Principles

### Coding to Abstractions
`DashboardService` never references a concrete metric class. It only knows about
`ListeningMetric<?>`, meaning the dashboard works correctly regardless of which
metrics are injected into it.

### Polymorphism
`DashboardService.compute()` iterates over a list of `ListeningMetric` objects
and calls `compute()` on each. Each call dispatches to a different implementation at
runtime, handling artists, tracks, and time through one shared interface.

### Dependency Injection
`DashboardService` receives its list of metrics through its constructor rather than
creating them itself. The caller controls which metrics the dashboard uses, making it
easy to swap implementations for testing or extend behavior without touching the service.