# RD Extension Demo

Demonstrates an extension module for [`rd` the Rundeck CLI Tool](https://github.com/rundeck/rundeck-cli). (Version 1.2+)

# Build

    ./gradlew build

# Use

You can try this extension after building it using `rd` version 1.2 or later:

    $ ./gradlew build
    $ RD_EXT_DIR=$PWD/build/libs rd ext demo
    A command was expected: rd ext demo [command]
    
    demo: subcommand demo
    
    Available commands:
    
       apicall    - Makes API call using Rundeck API Client interface, requires authentication
       apicustom  - Makes API call using custom API interface
       apiversion - displays the rundeck server API version
       example    - example command
       inputs     - example for parsing input
       scripting  - demonstrates basic data output for yaml/json scripting
       scripting2 - demonstrates typed data output for yaml/json scripting
    
    Use "rd ext demo [command] help" to get help on any command.

# Picocli 

**requires rd version 1.2.5 or later**

The Pico command demonstrates using Picocli to handle command argument and subcommand parsing

The build uses the 'shadow' gradle plugin to bundle Picocli into the extension jar so that it can be used standalone.

```
$ RD_EXT_DIR=$PWD/build/libs rd ext
A command was expected: rd ext [command]

ext: a test subcommand

Available commands:

   demo - subcommand demo
   pico - Demo use of picocli.

Use "rd ext [command] help" to get help on any command.
$ RD_EXT_DIR=$PWD/build/libs rd ext ext pico
What is your name? use --name <yourname>.  Or use wave to just wave at things
$ RD_EXT_DIR=$PWD/build/libs rd ext pico -n bob   
Name is bob
$ RD_EXT_DIR=$PWD/build/libs rd ext pico wave  
Missing required option '--thingies=<thingies>'
Usage: pico wave -t=<thingies> [-t=<thingies>]...
wazzup
  -t, --thingies=<thingies>

$ RD_EXT_DIR=$PWD/build/libs rd ext pico wave -t dirt -t sky
*waves at*
* dirt
* sky
```
