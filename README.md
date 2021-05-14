[![CodeFactor](https://www.codefactor.io/repository/github/kstenschke/referencer/badge)](https://www.codefactor.io/repository/github/kstenschke/referencer)  

# Referencer Plugin

Source code of the [Referencer plugin](http://plugins.intellij.net/plugin?pr=&pluginId=7104).
A plugin for the various intellij IDEs (working in IntelliJ IDEA Ultimate and Community Edition, 
CLion, RubyMine, WebStorm, PhpStorm, PyCharm, PyCharm CE, AppCode, Android Studio, etc)


## Table of Contents

* [Keyboard shortcuts and functionality](#keyboard-shortcuts-and-functionality)
* [Types of references to copy or insert](#types-of-references-to-copy-or-insert)
* [Types of references to go to](#types-of-references-to-go-to)
* [Changelog](#changelog)
* [Author and License](#author-and-license)


## Keyboard shortcuts and functionality

* Ctrl+Shift+Alt+C twice - Open list of references for copying to clipboard
* Ctrl+Shift+Alt+F twice - Replace occurrences of comfigured search/replace patterns in current document
* Ctrl+Shift+Alt+G twice - Open list of jump destinations in the current file for going to
* Ctrl+Shift+Alt+V twice - Open list of references for inserting into the current document

**Mac Users:** On Macs, the keyboard shortcuts are Shift+Alt+Cmd+C twice (copy), Shift+Alt+Cmd+V twice (paste) 
or Shift+Alt+Cmd+G twice (go)

The list can be cancelled via Esc key, navigated using cursor keys and items are selected using Enter.


## Types of references to copy or insert

All parsed items in the following list are relative to the current caret position,
for example "previous classname" refers to the first classname found when searching backwards starting
from the caret position. Some references are only specifically available for certain file types.

* Current date/time formatted as YYYY-MM-DD and YYYY-MM-DD HH:MM:SS
* Current UNIX timestamp in seconds and milliseconds

* Path to current file
* Filename
* Filename::line number
* Path to current file::line number
* When having a multi-line selection: File path / selection: line num. sel. start - line num. sel. end
* When having opened more than one file: list of paths to all opened files

* PHP files: list of method names in current file
* PHP files: previous classname::previous method name
* PHP files: previous classname::line number
* PHP files: previous classname
* PHP files: previous function name
* PHP files: previous/next variable name

* JavaScript files: namespace.classname
* JavaScript files: namespace.classname.previous method name
* JavaScript files: namespace.classname::line number
* JavaScript files: previous/next method name
* JavaScript files: file path formatted as namespace

* General word-completions: all continuations to the word to the left of the caret, found in the current file


## Types of references to go to

* Bookmarks in the current file
* PHP or JavaScript method declarations in the current file
* Markdown headlines in the current file
* Occurrences of the configured dynamic jump patterns in the current file


## Changelog

See [CHANGELOG.md](https://github.com/kstenschke/referencer-plugin/blob/master/CHANGELOG.md)


## Author and License

Copyright Kay Stenschke

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
