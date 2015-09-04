------------------ NEXT ------------------
* Robot
-- Press a special key to interrupt the bot.
- Record pressed keys
- Drag and Drop Event
- Duplicate add the event under the original and not on bottom

* File

* UI
- Selecting a line in the mainTable must select the corresponding line in events tables.
- Modify bot settings and bot informations in the gui.
- Solve bug when trying to move down many elements.
xx Upgrade code lisibility by using the id as a key and value as a LeftMouseEvent instead of using column number as a key.
- renommer le jtable2 en jtable1
- bug with keyboard timeout : not 7000.
- RecordMouse insert a line under the selected line and not at the end.
- Delete selected lines with the keyboard delete key.
- Print all availabled clicks in the GUI

------------------ v0.4 (00.08.2015) ------------------
* Robot
- Right click Event
- Wheele click Event
- DoubleLeftClick Event
- Possibility to resume bot at the selected line

* File

* UI


------------------ v0.3 (22.08.2015) ------------------
* Robot
- Possibility to get the mouse position in a new event.
- Comment methods.

* File
- Export file to external.
- Implement sleepDelay in the Keyboard table

* UI
- Better ui : lisibility upgraded.



------------------ v0.2 (03.08.2015) ------------------
* Robot
- Added KeyboardEvent

* File
- Import first file in the directory with the csv extension.

* UI
- Ability to move up and down events. Moving them will also moove lines in the event tables.
- Updating a comment in the main table will update it in the event tables.
- Updating a comment in the event tables will update it in the main table.
- Add Bot Events
- Duplicate Bot Events
- Delete Bot Events




------------------ v0.1 (10.07.2015) ------------------
* Robot
- Added LeftMouseEvent

* File
- Import file.

* UI
- 2 tables : one for the list of events and the other one for parameters.
- UI always on top
- Line is selected on event



