# Contributing to FX Deals Data Warehouse

Thank you for your interest in contributing to the FX Deals Data Warehouse project!

## Code of Conduct

Please be respectful and constructive in your interactions with other contributors.

## How to Contribute

### Reporting Bugs

1. Check if the bug has already been reported in the Issues section
2. If not, create a new issue with:
   - Clear title and description
   - Steps to reproduce
   - Expected vs actual behavior
   - Environment details (OS, Java version, etc.)
   - Relevant logs or screenshots

### Suggesting Features

1. Check existing issues and pull requests for similar suggestions
2. Create a new issue describing:
   - The problem you're trying to solve
   - Your proposed solution
   - Any alternatives you've considered

### Pull Requests

1. Fork the repository
2. Create a feature branch from `main`:
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. Make your changes following our coding standards
4. Write or update tests as needed
5. Ensure all tests pass:
   ```bash
   make test
   ```
6. Update documentation if needed
7. Commit with clear, descriptive messages
8. Push to your fork and submit a pull request

## Development Guidelines

### Code Style

- Follow standard Java conventions
- Use meaningful variable and method names
- Keep methods focused and concise
- Add JavaDoc comments for public APIs
- Use Jakarta EE patterns and best practices

### Testing

- Write unit tests for new features
- Maintain or improve code coverage (target: >80%)
- Test edge cases and error scenarios
- Use meaningful test names that describe what is being tested

### Commit Messages

Follow the conventional commits format:

```
type(scope): brief description

Detailed explanation if needed

Fixes #123
```

Types: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`

### Documentation

- Update README.md for new features
- Add API documentation for new endpoints
- Include code comments for complex logic
- Update configuration examples if needed

## Building and Testing

```bash
# Build the project
mvn clean install

# Run tests
mvn test

# Run with Docker
make deploy

# Clean up
make clean
```

## Questions?

Feel free to open an issue for questions or discussions about contributing.

Thank you for contributing! 🎉
