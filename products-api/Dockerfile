FROM gradle:8.9.0-jdk17

# Set a specific working directory
WORKDIR /app

# Copy the project files to the working directory
COPY --chown=gradle:gradle . ./

# Build the project
RUN gradle build -x test --no-daemon

# Expose the application port
EXPOSE 8081

# Run the application
CMD ["gradle", "bootRun"]
