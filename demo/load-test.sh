#!/usr/bin/env bash
set -euo pipefail

URL=${1:-http://localhost:8080/api/flash-sale/products/FS-001}
REQUESTS=${2:-1000}
CONCURRENCY=${3:-100}

printf 'Bắn %s request, concurrency=%s vào %s\n' "$REQUESTS" "$CONCURRENCY" "$URL"
seq "$REQUESTS" | xargs -P "$CONCURRENCY" -I{} curl -sS -o /dev/null -w '%{http_code}\n' "$URL" \
  | sort | uniq -c
