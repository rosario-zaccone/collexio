# Collexio

Collexio is a Java desktop application for managing personal collections. It separates the general description of an object from the concrete copies owned by the user.

## Terminology

- **Item specification**: the general object description, such as "Nintendo DS", "The Hobbit", or "Ficus elastica".
- **Item**: one concrete copy of an item specification, with its own status, photo, collection, and transactions.
- **Collection**: a group of items.
- **Transaction**: an income or expense associated with one item.

## Requirements

- JDK 17 or newer
- Maven 3.8 or newer
- Linux with `jpackage` and Debian packaging tools to build the optional `.deb` package

## First Launch

Collexio stores local data in:

```text
~/.collexio/
```

The first launch creates:

- `~/.collexio/data/collexio.db`
- `~/.collexio/images/`
- `~/.collexio/config.properties`

The setup dialog asks for a required contact email, used by scraping requests, and an optional Gemini API key, used only for AI-generated item descriptions.

## Run

```bash
mvn clean package
java -jar target/collexio-1.0.0.jar
```

## Test

```bash
mvn test
```

## Release Build

Use the release packaging script:

```bash
scripts/package-release.sh
```

The script runs the Maven package lifecycle, creates `dist/`, copies the executable jar, writes a SHA-256 checksum, and builds a Debian package when `jpackage` is installed.

Expected release outputs:

```text
dist/collexio-1.0.0.jar
dist/collexio-1.0.0.jar.sha256
dist/collexio_1.0.0_amd64.deb
```

## Install The Debian Package

```bash
sudo apt install ./dist/collexio_1.0.0_amd64.deb
```

After installation, run Collexio from the desktop application launcher or from the generated `collexio` command, depending on the desktop environment.

## Notes For Maintainers

- The default UI theme is Flat. The legacy 2000-style theme remains available from the View menu.
- Do not commit generated `target/` or `dist/` artifacts.
- Do not commit local data from `~/.collexio/`.
