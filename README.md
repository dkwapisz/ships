# Battleships
> Software Engineering Project
## Table of Contents
- [Features](#Features)
- [AI](#AI)
- [Technologies Used](#Technologies-Used)
- [Screenshots](#Screenshots)
- [How to open](#How-to-open)
- [Acknowledgments](#Acknowledgments)

## Features
The application implements the game "Battleships" on a [standard rules](https://en.wikipedia.org/wiki/Battleship_(game)).
- 3 game modes: Human vs Human, Human vs AI, AI vs AI
- 3 AI difficulties (Easy, Medium, Hard)
- Possibility of manual and random deployment of ships
- User registration and login
- Collecting game statistics for each player
- Saving replays of every game and replay them later

## AI
When playing with AI there are 3 difficulty levels available:
- EASY - AI shots randomly
- MEDIUM - AI shots randomly, but when it hits a ship, it tries to destroy it to the end (by shooting all around)
- HARD - AI plays like in Medium difficulty but have 20% chance to shots twice in his turn. If it doesn't shoot double, chance increases by 20% (up to 100%). If it shoot double, chance is reset to 0%.

## Technologies Used
- Java 17.0.1
- JavaFX Library + FXML (SceneBuilder)
- Maven
- MySQL Database (Local)

## Screenshots
![screenshot1](https://github.com/dkwapisz/ships/blob/main/src/main/resources/gameScreenshots/screenshot1.png)
![screenshot2](https://github.com/dkwapisz/ships/blob/main/src/main/resources/gameScreenshots/screenshot2.png)

## How to open 
### IntelliJ IDEA
1. Download project via Github
2. Open using IntelliJ IDEA
3. Reload Maven Dependencies
4. Run via LoginWindow.java class

## Acknowledgments
- Project was created in cooperation with [Mateusz Świder](https://github.com/maswi0118) and [Anna Młynarczyk](https://github.com/annamlynarczyk)
