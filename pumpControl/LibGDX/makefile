desktop/build/libs/desktop-1.0.jar: $(shell find . -path "./desktop/build" -prune -o -path "./.gradle" -prune -o -path "./.idea" -prune -o -print | tr '\n' ' ')
	(./gradlew clean desktop:dist)