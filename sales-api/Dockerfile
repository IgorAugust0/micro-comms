FROM node:22.3.0

# Set up pnpm
ENV PNPM_HOME="/pnpm"
ENV PATH="$PNPM_HOME:$PATH"
RUN corepack enable

# Set the working directory
WORKDIR /app

# Copy package files and install dependencies
COPY package*.json ./
RUN pnpm i

# Copy the rest of the project files
COPY . .

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["node", "--no-warnings", "--loader", "ts-node/esm", "src/server.ts"]
