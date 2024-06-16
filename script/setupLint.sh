#!/usr/bin/env bash

# Detekt Git Hooks pre-push 추가
LINTER_CURRENT_DIR=$(cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd)
GRADLEW_SCRIPT_PATH="$LINTER_CURRENT_DIR/../gradlew"
GIT_HOOK_DIR="$LINTER_CURRENT_DIR/../.git/hooks"
DETEKT_TARGET_FILE="$GIT_HOOK_DIR/pre-push"
KTLINT_TARGET_FILE="$GIT_HOOK_DIR/pre-commit"
DETEKT_HOOK_SCRIPT="$LINTER_CURRENT_DIR/hookScript"

if [[ ! -d "$GIT_HOOK_DIR" ]]
then
    mkdir -p "$DETEKT_SCRIPT_DIR"
fi

if [[ -f "$DETEKT_TARGET_FILE" ]]
then
    echo "*************************************************"
    echo "            Detekt integration failed            "
    echo "*************************************************"
    echo "Reason: GIT pre-push hook is already installed!"
    exit 1
fi

# KtLint Git Hooks pre-commit 추가
eval "$GRADLEW_SCRIPT_PATH addKtlintCheckGitPreCommitHook"
chmod +x "$KTLINT_TARGET_FILE"

cat "$DETEKT_HOOK_SCRIPT" > "$DETEKT_TARGET_FILE"
chmod +x "$DETEKT_TARGET_FILE"

echo "************************************************"
echo "                Linter installed                "
echo "************************************************"
echo "Install path: $(ls "$GIT_HOOK_DIR")"