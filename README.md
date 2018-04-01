# MA-GitHubCloner

A [MicroAnalyzer](https://github.com/joelBIT/MicroAnalyzer) plug-in for cloning GitHub repositories.

## How To Compile Sources

If you checked out the project from GitHub you can build the project with maven using:

```
mvn clean install
```

## Usage
Build the plugin jar and place it in the Java installation's */ext* folder. The return value of the overridden toString() method
corresponds to the parameter identifying the plug-in for MicroAnalyzer. By running the plug-in remote repositories hosted on GitHub 
are cloned to the local directory */repositories* created by MicroAnalyzer.
