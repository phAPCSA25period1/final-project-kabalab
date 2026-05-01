# ColorGrid: Interactive Button Grid Visualization

An interactive Java application that creates a dynamic grid of colored buttons with animation effects, color propagation, and hidden automated modes. Click, drag, and explore emergent patterns in a visual playground.

---

## 🎯 What This Software Does

**ColorGrid** is an interactive visual application featuring:
- A responsive 5×50 grid of colored buttons
- Click-triggered color animations and state changes
- Color propagation and similarity modes that blend colors from neighboring buttons
- Hidden control modes unlocked by rapid multiple clicks:
  - **Drag Mode** (5+ clicks on row 0, column 1): Enable click-on-hover behavior
  - **Similarity Mode** (5+ clicks on row 0, column 2): Toggle between random and predictive color changes
  - **Pattern Mode** (5+ clicks on row 0, column 0): Execute an automated sweeping animation across the grid
- Smooth transitions with 2-second delay before color reset

The application is part art, part interactive game—a playful exploration of state, animation, and emergent visual patterns.

---

## � Who It's For

- **Curious explorers** who enjoy interactive visual experiences
- **Students learning Java GUI programming** (Swing/JFrame)
- **Anyone interested in animated state machines** and color blend algorithms

---

## 🚀 How to Run the Program

### Prerequisites
- Java 17 or later
- No external dependencies (uses standard `javax.swing` library)

### Running the Application

1. **Compile the code:**
   ```bash
   javac -d bin src/*.java
   ```

2. **Run the program:**
   ```bash
   java -cp bin Main
   ```

A window titled "Button Test" will appear displaying the 5×50 grid.

### How to Interact

- **Click any button** to trigger color animation and state change
- **Hover over buttons** to see a lightened preview (unless drag mode is active)
- **Rapid-click the buttons in the first row** (top-left area) to discover hidden modes:
  - **Column 0 (leftmost)**: 5 clicks = sweep animation toggle
  - **Column 1 (second)**: 5 clicks = drag mode on/hover
  - **Column 2 (third)**: 5 clicks = similarity mode on/off

---

## 🔧 Technical Overview

### Main Classes

**`buttons` (Custom JButton)**
- Extends `JButton` with interactive state management
- Maintains position in 2D grid and color state
- Handles click animations, hover effects, and delayed color reset
- Static methods manage the shared button grid and control modes
- Implements color propagation algorithms and pattern automation

**`Main` (JFrame Application)**
- Creates the window frame and Swing panel
- Initializes a 5×50 grid of buttons
- Sets up GridLayout for button positioning
- Entry point for the application

### Key Data Structures

- **`buttons[][] buttonsList`**: 2D array storing all buttons in grid (indexed as [x][y])
- **`int[] pos`**: Each button's position [x, y] in the grid
- **`int[] size`**: Screen dimensions [width, height]
- **`int[] switches`**: Grid dimensions [columns, rows]

### Program Logic

1. **Boot sequence** (`buttons.boot()`): Initializes grid dimensions and storage
2. **Click handling** (`clicked()`):
   - Increments click counter
   - Darkens button color immediately
   - Schedules 2-second timer for color reset
   - Calls `handler()` if clicks >= 5
3. **Color modes**:
   - **Random mode**: New random soft color on reset
   - **Similarity mode**: Blends previous color with slight variation
   - **Directional propagation**: Averages RGB values from neighboring buttons (left, right, above)
4. **Pattern automation** (`btn0_0Clicked()`): Sweeps grid row-by-row with alternating directions, creating a wave effect

---

## Current Limitations
- Fixed 5×50 grid size (would benefit from dynamic sizing)
- Limited visual feedback for hidden mode activation
- No persistent state saving between runs
- Pattern automation hardcoded to specific trigger button

- - -

## 📝 Notes

This project demonstrates:
- Object-oriented design with encapsulation
- 2D array manipulation and grid-based systems
- Java Swing GUI programming
- State management and animation timing
- Algorithm thinking (color blending, pattern generation)
