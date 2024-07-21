# Car Traveling Around the Globe - Android Application

This project is part of the Advanced Computer Systems Engineering Laboratory (ENCS5150) and involves creating an Android application that demonstrates an animation of a car traveling around the globe.

## Features

- Sun rotating in and out of the screen
- Clouds moving across the screen
- Traffic light controlling the car movement
- Car rotating around the globe and interacting with a rock

## Assets Required

The following assets (all in PNG format with a transparent background) are required for the project:
1. Sun image
2. Cloud image
3. Planet Earth image
4. Car image
5. Rock image
6. 3 Traffic light images (red, orange, green)

## Animation Logic

### Sun Animation
- The sun rotates in and out of the screen.
- The rotation center for the sun is Earth's center.
- The sun starts rotating immediately upon opening the application and continues rotating infinitely.

### Cloud Movement
- Two cloud images move across the screen on the x-axis.
- One cloud is above the sun, and the other is behind it, ensuring proper layering.
- The translation duration for one cloud is 8 seconds, and for the other, it is 16 seconds.
- Both clouds start moving immediately upon opening the application and continue moving infinitely.

### Earth
- Planet Earth is stationary.
- The traffic light is positioned at the top center of the globe.
- The rock is positioned at the bottom center of the globe, flipped 180 degrees.
- The car starts in front of a red traffic light, which turns orange after 3 seconds and green after another 2 seconds.
- The car begins rotating around the globe clockwise at speed \(x\) after the traffic light turns green.
- When the car touches the rock, the car's speed changes to \(0.5x\), and the rock falls to the bottom left corner while rotating 180 degrees.
- The car continues rotating at \(0.5x\) speed until it reaches the traffic light again, repeating the cycle indefinitely.


