# Klondike
This project is an advanced implementation of Klondike Solitaire (including the Whitehead variant) written entirely in Java. It was developed as part of an Object-Oriented Design course at Northeastern University to demonstrate principles of modular architecture, abstraction, and test-driven development.

The program follows the Model–View–Controller (MVC) design pattern to ensure flexibility and separation of concerns.

The model encapsulates the game’s state, move rules, and card logic, including specialized Whitehead rules where all cards are visible and moves must follow single-suit descending sequences.

The controller interprets user commands and mediates interactions between the view and model while enforcing input validation and exception safety.

The view renders the game state textually, producing a clear and consistent terminal interface.

A key focus was ensuring robustness and reusability through interface abstraction and composition over inheritance. The project supports both Basic and Whitehead variants without duplicating code. Comprehensive JUnit 5 tests verify the correctness of game logic, move legality, and edge-case handling, resulting in a perfect test pass rate across over 40 automated checks.

This repository reflects professional-grade software engineering practices, including clean architecture, error resilience, and code extensibility, serving as a foundation for adding future variants, graphical interfaces, or AI-driven gameplay.
