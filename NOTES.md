# NOTES.md

## Summary of changes

I focused on high-impact search and pagination correctness issues within the timebox.

1. Fixed the task search SQL precedence bug in `TaskRepository.java` and aligned the H2/Oracle reference SQL. The title/description search is now grouped before applying the status and archived filters.

2. Improved pagination by using Spring Data `Pageable` and a `countQuery`, so the database returns only the requested page and accurate totals.

3. Fixed frontend request state handling. Failed requests now clear loading, new requests clear old errors, in-flight requests are aborted when filters change, and changing search/status resets back to page 1.

## What I chose not to change

I did not add create/update/delete features, authentication, or broad UI redesigns because they are outside the patch exercise scope. I also left the artificial backend delay in place since it appears intended to expose frontend race/loading behavior.

## Biggest remaining risk

The API still has minimal validation and error shaping; invalid parameters can produce generic server errors instead of consistent client-facing responses.

## Tools / AI usage

I used ChatGPT/Codex to inspect the codebase, identify likely planted bugs, apply focused patches, and run verification commands. I reviewed the changes manually and kept the diff limited.
