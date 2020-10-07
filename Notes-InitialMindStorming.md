# Initial Mind-storming

### Requirements

1. We draw the digit.
2. Click on one of the buttons. (Simple and CNN)
3. Result is displayed.

### Goals

1. Everything in a single package. [DONE]
2. Adopt MVC.
    1. Right now, everything (mode actions, UI changes, etc.) are lumped into action handlers. These need to be refactored.
    2. Unrelated code (like machine learning) should be removed from the UI.
3. Create updated class diagram while we develop.
4. Run creates Progress Bar. This should be done in the UI.
5. For elements hold reference to MainFrame, implement a callback function that wraps whatever needs to be done with MainFrame. (Functional Equivalent of Builder Pattern)
6. A central interface (`Recognizer`) between UI and (SimpleNN/CNN)
    Recognizer will be mix of Strategy+Factory
    [Link to chat](https://fse-engineering-asu.slack.com/archives/G01BU42BB1Q/p1601937394055600?thread_ts=1601934777.024900&cid=G01BU42BB1Q)
7. Refactor detectHorizontalEdges detectSobelEdges and detectVerticalEdges with or without pattern. (Mention the justification for whatever is done in the final document)

