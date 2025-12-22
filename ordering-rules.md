# Ordering Rules – Shadow Ledger System

## Objective
Ensure deterministic, reproducible ordering of financial events so that the
shadow ledger always computes the same balance regardless of Kafka delivery order.

## Problem
Kafka does not guarantee strict ordering across partitions.
Duplicate or out-of-order events can lead to incorrect balances.

## Ordering Strategy
All ledger computations follow a strict ordering rule:

1. **Primary key: timestamp**
2. **Secondary key: eventId (lexicographical)**

This guarantees:
- Stable ordering even if two events share the same timestamp
- Idempotent reprocessing
- Deterministic replay from Kafka or database

## Deduplication Rule
- `eventId` is globally unique
- Any event with an already-seen `eventId` is ignored
- Deduplication occurs **before insertion** into the ledger table

## Ledger Table Properties
- Append-only (immutable)
- No updates or deletes
- Reprocessing Kafka messages does not change balances

## SQL Implementation Example
```sql
SELECT
  account_id,
  SUM(
    CASE
      WHEN type = 'credit' THEN amount
      WHEN type = 'debit' THEN -amount
    END
  ) OVER (
    PARTITION BY account_id
    ORDER BY event_timestamp, event_id
    ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
  ) AS running_balance
FROM ledger_events;
