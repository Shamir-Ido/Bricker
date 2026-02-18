# **Bricker – OOP Game**
<img width="875" height="635" alt="image" src="https://github.com/user-attachments/assets/deda4a78-a924-489e-9fa1-8986e79e0e46" />

---

## **Basic Game Design**

The game is a classic Arkanoid-style brick breaker:

- **Gameplay:** The player moves a paddle horizontally to bounce a ball that breaks bricks. The ball must not fall below the paddle.  
- **Ball:** Tracks collisions using `getCollisionCounter()`.  
- **Paddle:** Moves based on player input and can interact with balls.  
- **Bricks:** Removed on collision using a `CollisionStrategy` pattern for flexibility.  
- **Life Display:**  
  - **Graphical:** Icons representing remaining lives.  
  - **Numeric:** Updates the life count on screen.

**Design Choice:**  
- Used the **Strategy Pattern** for brick collisions to allow adding new behaviors in Part 2 without modifying existing brick logic.  
- This keeps the code **extensible, maintainable, and adheres to OOP principles**.  

---

## **Special Brick Behaviors**

Implemented five special behaviors for bricks:

1. **Extra Balls (Puck):**  
   - Spawns 2 smaller balls at the brick’s center.  
   - Random upward direction.  
   - Collides with other pucks, paddle, bricks, and main ball.  
   - Removed if out of bounds.  

2. **Extra Paddle:**  
   - Adds a second paddle that mirrors player input.  
   - Appears at screen center and disappears after 4 hits.  
   - Only one extra paddle can exist at a time.  

3. **Turbo Mode:**  
   - Activates for the main ball only.  
   - Multiplies speed ×1.4 and changes color to red.  
   - Reverts after 6 collisions.  

4. **Life Restore:**  
   - Drops a heart from the brick’s center.  
   - Can only be collected by the original paddle.  
   - Increases the life count up to the maximum.  

5. **Double Behavior:**  
   - Combines two random special behaviors.  
   - Ensures no more than 3 special behaviors per brick.  
   - Allows repeating the same behavior (e.g., multiple extra balls).  

**Design Notes:**  
- Each special behavior is implemented as a **separate strategy**, wrapped by `DoubleBehavior` if needed.  
- The main ball is tracked to ensure certain behaviors only apply to it.  
- Collision counting is used to manage temporary effects like turbo mode.  

---

- **Packages:** Organized for clarity and maintainability.  
- **Single Responsibility:** Each class handles one type of behavior.  
- **Extensibility:** New brick behaviors can be added without modifying existing code.  

---

## **Implementation Highlights**

- **Collision Handling:** Centralized in strategies; ensures scalability for multiple bricks and balls.  
- **Paddle Mirroring:** Extra paddle follows the player while respecting screen bounds.  
- **Object Cleanup:** Balls, hearts, and extra paddles are removed from the game when out of bounds.  
- **Randomization:** Special behaviors are randomly assigned to bricks while respecting probability rules.  
- **API Integrity:** Part 1 API is preserved; Part 2 added new strategies without breaking existing code.  

---

## **Additional Notes**

- The game supports **multiple balls**, **special paddle behaviors**, and **visual/numeric life display**.  
- Uses **DanoGameLab 1.1.0** engine for object management, collisions, and rendering.  
- Fully OOP-compliant: strategy pattern, clean inheritance, and minimal coupling.


