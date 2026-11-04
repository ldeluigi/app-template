#!/usr/bin/env bash
# Makes the template your app: its name, its id (Android application id and iOS bundle id, permanent once on a store)
# and its code's package, everywhere. Run it once, from the repository's root, right after creating the repository.
# Usage: scripts/make-it-yours.sh "<Name>" <app.id> [<code.package>]     (the package is the id by default)
# Example: scripts/make-it-yours.sh "My App" com.example.myapp
set -euo pipefail

name="$1"
id="$2"
package="${3:-$2}"
old="app.template"

valid='^[a-z][a-z0-9_]*(\.[a-z][a-z0-9_]*)+$'
[[ "$id" =~ $valid ]] || { echo "The id must look like com.example.myapp (lower case, dots)." >&2; exit 1; }
[[ "$package" =~ $valid ]] || { echo "The package must look like com.example.myapp (lower case, dots)." >&2; exit 1; }
grep -q "^app.package=$old$" gradle.properties || { echo "This is no longer the template: nothing to do." >&2; exit 1; }

# The identity, in one place
sed -i -e "s|^app.name=.*|app.name=$name|" -e "s|^app.id=.*|app.id=$id|" -e "s|^app.package=.*|app.package=$package|" \
  gradle.properties

# The Kotlin code: its folders, then the package in every file
old_path="${old//.//}"
new_path="${package//.//}"
find . -type d -path "*/kotlin/$old_path" -not -path "./.git/*" -not -path "*/build/*" | while read -r dir; do
  root="${dir%/"$old_path"}"
  mkdir -p "$root/$(dirname "$new_path")"
  git mv "$dir" "$root/$new_path"
  rmdir -p "$root/$(dirname "$old_path")" 2>/dev/null || true
done
old_pattern="${old//./\\.}"
grep -rlE --include='*.kt' --include='*.kts' "$old_pattern" . | grep -v '/build/' | while read -r file; do
  sed -i -E "s/(^|[^a-zA-Z0-9_])$old_pattern([^a-zA-Z0-9_]|$)/\1$package\2/g" "$file"
done

# The first line of the README names the app
sed -i "1s/.*/# $name/" README.md

# The imports, sorted again for the new package's name
./gradlew ktlintFormat --quiet || echo "Run ./gradlew ktlintFormat to sort the imports again." >&2

echo "Done: $name ($id), code in $package. Next:"
echo "  1. Describe the app in AGENTS.md (\"The app\") and README.md."
echo "  2. ./gradlew check, then commit and push."
echo "  3. app-machinery's docs/02-new-app.md, from step 3."
