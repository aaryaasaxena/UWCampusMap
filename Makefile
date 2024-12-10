# Define the folder containing your source files
SRC_DIR = .
TEST_DIR = $(SRC_DIR)
JUNIT_JAR = ../junit5.jar

# Specify the individual classes to compile
CLASSES = Frontend.java Backend.java DijkstraGraph.java HashtableMap.java
CLASS_FILES = $(CLASSES:.java=.class)

# Define the main server class to run
SERVER_CLASS = WebApp

# Targets
runServer: $(CLASS_FILES)
	sudo java -cp $(SRC_DIR) $(SERVER_CLASS) 8000  # Run the WebApp with port 8000 using sudo

runTests: $(CLASS_FILES)
	java -cp $(SRC_DIR):$(JUNIT_JAR) org.junit.platform.console.ConsoleLauncher --scan-classpath

clean:
	rm -f $(CLASS_FILES)

# Compile specific Java files
%.class: %.java
	javac -cp $(SRC_DIR) $<
