# personal-projects
This is a Minesweeper clone written in Java in 2018 using the JavaFX library.

## **Setup (for Linux/Mac)**
Make sure to install JavaFX by following the instructions on the link https://gluonhq.com/products/javafx/.

```export PATH_TO_FX=path/to/javafx-sdk-<current-version>/lib```
Compile the java file with these flags: ```javac --module-path $PATH_TO_FX --add-modules javafx.controls Minesweeper.java```
Run the program with: ```java --module-path $PATH_TO_FX --add-modules javafx.controls Minesweeper```
Refer to https://openjfx.io/openjfx-docs/#install-javafx for installation issues. The program will ask for four parameters before launching:
- Square size. This is how large each square will be on our minefield in terms of pixels. Recommended values are between 25-60.
- Width & height. This is how large the window, and therefore the minefield will be, in terms of pixels. Recommended values are between 400-600 for each. This depends on your screen resolution.
- Mine count. This controls how many mines'll be present in our field. Divide your width and height by the square size and multiply them together to see how many squares you'll have in total. Easy: 10% of the total square count. Medium: 25% of the total square count. Hard: 40+% of the total square count.

Enjoy!
