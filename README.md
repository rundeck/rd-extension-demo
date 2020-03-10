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
