Referencer Changelog
====================

**1.4.1 - 2021-02-26**
* Add info about the amount of lines, that document is reduced/grown by after replace action
* Fix IDE warnings during replace action: Wrap document modification into WriteCommandAction lambda

**1.4.0 - 2021-02-25**
* Add multitenancy: Add export/import buttons to plugin settings
* Improve performance (Modernize Java-5 left-overs, replace usages of toArray w/ pre sized array) 
* Bugfix: GoTo-Pattern search did parse all line when no patterns given

**1.3.0 - 2021-02-21**
* Simplify/Clarify instructions, descriptions and labels
* Change pattern definitions separator from colon to tab
* Add replace action
* Add regions recognition to built-in GoTo targets
* Add referencer actions into more obvious IDE menus
* Add plugin icon

**1.2.1 - 2012-02-12**
* Ensure editor re-grabs focus after using referencer
* Change README and CHANGELOG format from textile to markdown
* Add Markdown headlines to "GoTo" destinations
* Correct preferences textarea behavior (caret-offset was reset on modification)
* Partly modernize source code

**1.2.0 - 2017-07-03**
* Update plugin settings implementation to be compatible w/ more recent Open API
* Add option to copy or insert list of all method names from current file
* Improve PHP file methods indexer
* Improve Darcula compatibility
* Modernize source code

**1.1.1 - 2014-08-04**
* Sort "GoTo..." jump destination from configured patterns alphabetically
* Sort "GoTo..." methods alphabetically
* Make section headers within options popup more visually obvious
* Bugfix: Destination patterns were utilized even if not yet stored

**1.1.0 - 2014-08-02**
* Add: plugin settings with dynamic "GoTo..." jump patterns
* Add: PHP and JavaScript methods listing in "GoTo..." menu
* Bugfix: IndexOutOfBoundsException in Goto bookmark action
* Bugfix: Line numbers in GoTo bookmark action were displayed one too high
* Reduce changelog to previous five versions, added separate full changelog

**1.0.12 - 2013-12-03:** Bugfix: Opening Referencer on first character of document caused IndexOutOfBoundsException

**1.0.11 - 2013-11-28**
* Add notification when there are no bookmarks for going to
* Improve compatibility: Compiled with JDK target bytecode version 1.6 (was 1.7)

**1.0.10 - 2013-11-28:** Add context menu to GoTo action: remove all bookmarks from current file

**1.0.9 - 2013-11-05:** Add action: GoTo bookmarks

**1.0.8 - 2013-11-04**
* Update API to IC-129.713
* Improve UI, added Darcula Theme compatibility

**1.0.7 - 2012-12-10**
* Improve general word-completions detection
* Add respective caret movement to text insertion

**1.0.6 - 2012-11-16**
* Improve references list with section headers
* Add list of all open files
* Add UNIX timestamp in seconds and milliseconds

**1.0.5 - 2012-11-03:** Add item group separators

**1.0.4 - 2012-11-02:** Add references of all endings to occurrences of the word at the caret

**1.0.3 - 2012-11-02:** Add date and timestamp references

**1.0.2 - 2012-10-09:** Add namespace reference from JavaScript filepath

**1.0.1 - 2012-09-30:** Add preference retaining (selected reference per file type)

**1.0.0 - 2012-09-29:** Initial release
