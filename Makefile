# Define the folder containing your source and test files
SRC_DIR = .
TEST_DIR = $(SRC_DIR)
JUNIT_JAR = ../junit5.jar
CLASSES = $(wildcard $(SRC_DIR)/*.java)
CLASS_FILES = $(CLASSES:.java=.class)

# Define the main server class to run
SERVER_MAIN = ServerMainClassNameHere

# Targets
runServer: $(CLASS_FILES)
	java -cp $(SRC_DIR) $(SERVER_MAIN)

runTests: $(CLASS_FILES)
	java -cp $(SRC_DIR):$(JUNIT_JAR) org.junit.platform.console.ConsoleLauncher --scan-classpath

clean:
	rm -f $(CLASS_FILES)

# Compile all Java files
%.class: %.java
	javac -cp $(SRC_DIR):$(JUNIT_JAR) $<
