**1.2.0 - Not tagged yet**
* Updated plugin settings implementation to be compatible w/ more recent Open API
* Added option to copy or insert list of all method names from current file
* Improved PHP file methods indexer
* Improved Darcula compatibility
* Modernized source code
* Change ChANGELOG format from textile to markdown

**1.1.1 - 2014-08-04**
* Sorted "Go To..." jump destination from configured patterns alphabetically
* Sorted "Go To..." methods alphabetically
* Made section headers within options popup more visually obvious
* Bugfix: Destination patterns were utilized even if not yet stored

**1.1.0 - 2014-08-02**
* Added: plugin settings with dynamic "Go To..." jump patterns
* Added: PHP and JavaScript methods listing in "Go To..." menu
* Bugfix: IndexOutOfBoundsException in go to bookmark action
* Bugfix: Line numbers in go to bookmark action were displayed one too high
* Reduced changelog to previous five versions, added separate full changelog

**1.0.12 - 2013-12-03:** Bugfix: Opening Referencer on first character of document caused IndexOutOfBoundsException

**1.0.11 - 2013-11-28**
* Added notification when there are no bookmarks for going to
* Improved compatibility: Compiled with JDK target bytecode version 1.6 (was 1.7)

**1.0.10 - 2013-11-28:** Added context menu to go action: remove all bookmarks from current file

**1.0.9 - 2013-11-05:** Added action: go to bookmarks

**1.0.8 - 2013-11-04**
* Updated API to IC-129.713
* Improved UI, added Darcula Theme compatibility

**1.0.7 - 2012-12-10**
* Improved general word-completions detection
* Added respective caret movement to text insertion

**1.0.6 - 2012-11-16**
* Improved references list with section headers
* Added list of all open files
* Added UNIX timestamp in seconds and milliseconds

**1.0.5 - 2012-11-03:** Added item group separators

**1.0.4 - 2012-11-02:** Added references of all endings to occurrences of the word at the caret

**1.0.3 - 2012-11-02:** Added date and timestamp references

**1.0.2 - 2012-10-09:** Added namespace reference from JavaScript filepath

**1.0.1 - 2012-09-30:** Added preference retaining (selected reference per file type)

**1.0.0 - 2012-09-29:** Initial release