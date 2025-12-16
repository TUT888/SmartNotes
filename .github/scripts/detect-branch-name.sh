#!/bin/bash

# Exit immediately if any command fails
set -e

echo "Detecting Branch Name..."

if [[ "$BRANCH_NAME" == *"note"* ]]; then
  echo "test_tag=note" >> $GITHUB_OUTPUT
fi