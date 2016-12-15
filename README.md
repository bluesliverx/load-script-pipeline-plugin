# Jenkins Load Script Pipeline Plugin

This simple plugin adds a loadScript function in pipeline scripts that may be used for running pre-loaded
Groovy scripts with support for replaying builds. While `evaluate()` may be used for this as well, it does not 
support modifying scripts using replay.