# MEowTRO

## Game Overview

MEowTRO is a subway planning game. In the city, passengers appear from various locations and designate destination stations in other areas. At the start of the game, players have initial funding and must use it to invest in resources like stations, routes, railways, locomotives, carriages, bridges, tunnels, and other construction resources. The goal is to build a subway system that efficiently transports passengers to their destinations and collects ticket fares. Passengers’ satisfaction, which reflects how smoothly they reach their destinations, indirectly impacts monthly bonuses. Throughout the game, unexpected events such as festivals, holidays, fires, and earthquakes may disrupt operations by affecting passenger generation speed in various areas or the reliability of the subway system. The game features two modes: in “MaxProfit” mode, players aim to earn as much money as possible within a limited time; in “SpeedRun” mode, they must reach a financial target in the shortest time possible.

## Model Design

class Diagram
![class Diagram](https://i.imgur.com/91Ub390.jpg)


### Key Class Descriptions

**Game, GameFactory**

The `Game` class manages the game flow, holding the city (`City`), game configuration file (`Config`), event trigger module (`EventTrigger`), game termination module (`GameTerminateChecker`), and account balance. It is generated through the `createGame()` method in `GameFactory`. Each update in `Game` refreshes the timeline and current city status.

**Config**

`Config` records numerous parameters within the game, generated based on specified `defaultConfig` and `localConfig` files, and offers a `get()` method to access corresponding values through keys.

**Timeline**

Manages the game’s timeline using the Singleton pattern, recording the current year, month, day, hour, minute, and second. Each update progresses one time unit (`timeUnit`) and provides methods to obtain the current time and compare the sequence of two moments.

**City**

Describes the entire city, recording all regions (`Region`), obstacles (`Obstacle`), subway lines (`Line`), the total number of transported passengers (`transportedPassengerCount`), and the game it belongs to. Methods are available for adding and removing subway lines, retrieving a random region, and obtaining a specified number of stations outside a given region. Each update refreshes all regions and subway lines.

**Region**

Describes a region within the city, recording all points within the area (`position`), passenger spawn rate (`spawnRate`), all passengers generated in the area (`passengers`), all stations within the area (`stations`), passenger satisfaction within the area (`satisfactions`), total passengers generated in the area who successfully reached their destination (`transportedPassengerCount`), and the city it belongs to. Each update refreshes all passengers generated in the area and all stations within it. Additionally, based on the current passenger spawn rate, new passengers may appear at random positions within the area.

**Obstacle, Mountain, River**

Describes obstacles in the city, divided into `Mountain` and `River`. Both classes inherit from the abstract class `Obstacle`, recording all position points (`position`) in the area.

**Passenger, CutInLineElder, ShortestPathCalculator**

`Passenger` describes a passenger, recording details such as the region of origin (`birthRegion`), current position (`position`), time of creation (`spawnTime`), lifespan (`lifeTimeLimit`), destination station (`destinationStation`), current station (`currentStation`), current carriage (`currentCar`), number of stations passed (`traveledStationCount`), and status (`state`).

Passengers behave according to their state: in the `WALKING` state, they approach the nearest station at a walking speed and switch to `AT_STATION` upon arrival; in `AT_STATION`, they queue at the station, boarding the train if its direction aligns with their shortest path, switching to `TRAVELING`; in `TRAVELING`, they are inside a carriage, moving with the train. Upon reaching a station, they decide whether to alight based on their shortest path. If they reach the destination, they enter the `ARRIVED` state. The shortest path is calculated by the `ShortestPathCalculator`, determining the minimum stations needed from start to destination, helping passengers decide on boarding or alighting. With each update, passengers check if they are near death, displaying a corresponding animation; if they die, they are removed from the region. Upon reaching their destination or death, passengers report their satisfaction to their origin region.

`CutInLineElder` describes a special type of passenger who cuts to the front of the queue upon entering a station. Other behaviors align with general passengers.

**Station**

Describes a station, recording location (`position`), passenger queue (`queue`), connected railways (`railways`), subway lines passing through (`lines`), station level (`level`), and the maximum number of subway lines allowed (`maxLineNum`). It provides the `getAdjacents()` method to retrieve all adjacent stations connected by railways. Each update recalculates passenger display positions based on the current queue.

**Railway, RectangularRailwayRealizer**

`Railway` describes the railway connecting two stations, recording the subway line it belongs to (`Line`), starting station (`start`), ending station (`end`), all locomotives operating on it (`locomotives`), and remaining railway lifespan (`remainTimeToLive`). Its graphical path is handled by `RectangularRailwayRealizer`, considering all city stations to avoid crossing. `Railway` also provides methods for moving locomotives and carriages in the travel direction and determining their new virtual positions after movement.

**RailwayDecorator, Bridge, Tunnel**

`RailwayDecorator` describes railway decorations and is an abstract class. `Bridge` and `Tunnel` inherit from `RailwayDecorator`, representing bridges and tunnels required for crossing rivers and mountains, respectively.

**Line**

Describes a subway line formed by interconnected railways, recording the railways (`railways`), stations passed through (`stations`), locomotives traveling on it (`locomotives`), the line's color (`color`), and the city it belongs to (`City`). During updates, all locomotives on the line are refreshed.

**Locomotive, Car**

A train consists of one `Locomotive` connected to zero or more `Car` objects. The `Locomotive` records all cars connected behind it (`cars`), the railway it is currently on (`railway`), its current position (`position`), direction of movement (`direction`), locomotive level (`level`), level-to-max-speed mapping (`levelToMaxSpeed`), level-to-max-car count mapping (`levelToMaxCar`), state (`state`), list of passengers wanting to board (`getUpQueue`), and list of passengers wanting to alight (`getDownQueue`). The `Car` records all passengers inside (`passengers`), maximum passenger capacity (`capacity`), associated locomotive (`locomotive`), and current position (`position`).

During updates, both `Locomotive` and `Car` act based on the locomotive’s current status. In `MOVING` state, the locomotive acquires a new position on the railway and determines if it should change direction. Upon reaching the next station, it enters `ARRIVE_DROP` state, where it checks if passengers in `getDownQueue` want to get off, updating the queue. Then it inquires if any passengers in the station’s queue want to board, updating `getUpQueue`. In `ARRIVE_DROP` state, a passenger exits the car into the station from `getDownQueue`; if empty, it switches to `ARRIVE_GETON` state. In `ARRIVE_GETON` state, if there’s space in any car, a passenger from `getUpQueue` boards; once `getUpQueue` is empty, it returns to `MOVING` state.

**EntityManager, StationManager, RailwayManager, LocomotiveManager, CarManager**

`EntityManager` is an abstract class inherited by `StationManager`, `RailwayManager`, `LocomotiveManager`, and `CarManager`, each responsible for checking and performing construction or removal of stations, railways, locomotives, and cars according to set conditions.

**Event, HolidayEvent, RushHourEvent, NewYearEvent, DisasterEvent, FireEvent, EarthQuakeEvent, EventTrigger**

`Event` is an abstract class that describes unexpected events, recording the city (`city`) it belongs to and the occurrence time (`happenedTimeString`). `HolidayEvent` and `DisasterEvent` are also abstract classes inheriting from `Event`. `RushHourEvent` and `NewYearEvent` inherit from `HolidayEvent`, recording the increase rate of passenger generation and affecting a random region or all regions when triggered. `FireEvent` and `EarthQuakeEvent` inherit from `DisasterEvent`, recording the remaining lifespan proportion and affecting a random railway or all railways when triggered.

`EventTrigger` records all events and checks the current time to trigger events at the appropriate moments.

**GameTerminateChecker, MaxProfitMode, SpeedRunMode**

`GameTerminateChecker` is an abstract class that determines game termination conditions. `MaxProfitMode` and `SpeedRunMode` inherit from `GameTerminateChecker`.

**OnClickEvent, StationBuilder, RailwayBuilder, LocomotiveBuilder, CarBuilder, Upgrader, Destroyer, WaitForClick**

`OnClickEvent` is an abstract class that handles click events on the screen. `StationBuilder`, `RailwayBuilder`, `LocomotiveBuilder`, `CarBuilder`, `Upgrader`, `Destroyer`, and `WaitForClick` handle actions like adding stations, railways, locomotives, cars, upgrading, destroying objects, and waiting for clicks, respectively.

**MyButton, StationButton, RailwayButton, LocomotiveButton, CarButton, UpgradeButton, DestroyButton, PlayButton, PauseButton, FastForwardButton**

`MyButton` is an abstract class representing game buttons, with a recorded state and an `onClick()` method. `StationButton`, `RailwayButton`, `LocomotiveButton`, `CarButton`, `UpgradeButton`, and `DestroyButton` inherit from `MyButton`, representing buttons for adding stations, railways, locomotives, cars, upgrading, and destroying, each triggering a corresponding `OnClickEvent` in `onClick()`.

`PlayButton`, `PauseButton`, and `FastForwardButton` also inherit from `MyButton`. In `onClick()`, they modify the `AnimationTimer` to control normal play, pause, and fast-forward time effects.

### Discussion

After listing the detailed game rules within the team, we found that many rules operate similarly, differing only in condition checks or slightly varying affected objects, as shown below:

1. Game termination conditions are divided into "Maximizing Profit within a Time Limit" and "Achieving Target Profit in the Shortest Time," each checking time and balance, while both perform actions on `Game`.
2. Mountains and rivers both act as obstacles but require different railway decorations.
3. Commuter, New Year, fire, and earthquake events all affect specific objects (regions or railways) at designated times, though the number affected varies (one random or all).
4. Numerous buttons on the game screen perform actions like construction, destruction, or upgrades upon being pressed.
5. General passengers and queue-cutting elders have mostly similar behaviors, differing only in queuing behavior upon entering a station.

For these behaviors, we abstracted the similar aspects, forming `GameTerminateChecker`, `Obstacle`, `Event`, `MyButton` (with corresponding `OnClickEvent`), and allowed `CutInLineElder` to inherit from `Passenger`. This design makes it easy to add new game modes, obstacle types, events, buttons, or passenger types in the future by simply extending those parts without altering other code.

To capture interactions between objects within the subway system, we allowed each class in the subway system to handle only the logic for interacting with other objects within the system. For more complex algorithms, we delegated the implementation to other classes, such as `ShortestPathCalculator` for shortest path planning and `RectangularRailwayRealizer` for determining railway turns during construction.

Additionally, we extracted numerous constants in the game, such as city map files, passenger walking speed, maximum train speed, and event times, into separate `.properties` files. These values are read at runtime using `Config`, allowing different levels to be created by merely modifying the `.properties` files.

However, our design has some limitations.

For ease of access during implementation, we often hold references in multiple places. For instance, `Railway` can access locomotives currently running on it, though not every railway will have a locomotive at all times, and locomotives should ideally be stored by `Line`. While this approach facilitates updating locomotive positions and is more robust, it leads to tightly coupled design. Additionally, storing `Config` and `balance` as static variables in `Game` is also not ideal.

## Environment

This project is built around Java 11, using the open-source JavaFX 11 framework for handling graphical interfaces.

## How to Play the Game

The game interface is divided into the top row, left button panel, right button panel, and the central city map.

**Top Row**

The top row displays the game time, with three buttons below it, from left to right: normal speed, pause, and fast forward, allowing you to control the game speed. In all three modes, you can construct or demolish various components of the subway system.

**City Map**

Green and blue areas represent mountains and rivers, respectively, while each other colored block represents a region. Large icons indicate stations, and small icons indicate passengers, with each passenger’s destination matching a station of the same icon.

**Left Button Panel**

Each button in the left panel corresponds to a construction or demolition function and displays the associated cost. From top to bottom, the buttons and their functions are as follows:

1. **Add Station**: Click this button and then click any region on the city map to construct a new station at that location.
2. **Add Locomotive**: Click this button and then click any existing railway on the city map to add a locomotive to the corresponding subway line. Note that this locomotive cannot carry passengers, and the number of carriages it can attach is limited by its level.
3. **Add Carriage**: Click this button and then click any existing locomotive on the city map to add a carriage for passenger space.
4. **Upgrade**: Click this button and then click any existing station or locomotive on the city map to upgrade it. Upgraded stations accommodate more waiting passengers; upgraded locomotives have higher speeds and increased carriage capacity.
5. **Repair Railway**: Click this button and then click any existing railway on the city map to repair it, extending its remaining lifespan.
6. **Demolish**: Click this button and then click any station or subway line. Clicking a station will remove the station, all routes passing through it, and all locomotives and carriages on those routes. All passengers waiting at the station will also die. Clicking a subway line will remove the line, along with any locomotives and carriages on it.
7. **Add Railway**: Click this button and then click two stations you wish to connect to build a railway between them.
8. **Bridge**: Displays the cost only and is non-clickable. If a railway crosses a river, a bridge will be automatically purchased.
9. **Tunnel**: Displays the cost only and is non-clickable. If a railway crosses a mountain, a tunnel will be automatically purchased.

**Right Button Panel**

The right button panel is color-coded to represent six subway lines. When constructing railways, click the desired line color, then click two stations you wish to connect to build a railway between them.
