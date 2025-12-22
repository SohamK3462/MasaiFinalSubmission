#!/bin/bash

echo "Starting Shadow Ledger acceptance tests..."

GATEWAY=http://localhost:8080
TOKEN="Bearer sample-jwt-token"

echo "1. Posting valid event"
curl -X POST $GATEWAY/events \
  -H "Authorization: $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "eventId": "E1001",
    "accountId": "A10",
    "type": "credit",
    "amount": 500,
    "timestamp": 1735561800000
  }'

echo "2. Fetching shadow balance"
curl $GATEWAY/accounts/A10/shadow-balance \
  -H "Authorization: $TOKEN"

echo "3. Running drift check"
curl -X POST $GATEWAY/drift-check \
  -H "Authorization: $TOKEN" \
  -H "Content-Type: application/json" \
  -d '[{"accountId":"A10","reportedBalance":450}]'

echo "Acceptance tests completed."
