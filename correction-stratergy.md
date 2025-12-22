
---

## 📄 `correction-strategy.md`

```md
# Correction Strategy – Drift and Correction Service

## Objective
Detect mismatches between the Core Banking System (CBS) balance and the shadow
ledger, and automatically correct simple discrepancies.

## Inputs
- CBS balance file (JSON array)
- Shadow ledger balances from Postgres

## Drift Detection Logic
For each account:


### Drift Classification

| Drift Value | Interpretation |
|-----------|----------------|
| drift = 0 | No issue |
| drift > 0 | Missing credit |
| drift < 0 | Incorrect debit |
| unknown pattern | Manual review |

## Automatic Corrections
Only **simple mismatches** are auto-corrected.

### Missing Credit
- Generate a credit correction event
- Amount = drift

### Incorrect Debit
- Generate a reversal credit
- Amount = absolute(drift)

### Unknown Cause
- Logged for manual investigation
- No Kafka event produced

## Correction Event Rules
- New unique `eventId`
- Produced to `transactions.corrections`
- Consumed by Shadow Ledger Service
- Subject to same ordering and deduplication rules

## Manual Correction
Admin-triggered endpoint:


- Used for edge cases
- Publishes correction event directly

## Safety Guarantees
- No destructive updates
- Corrections are fully auditable
- Ledger remains append-only

