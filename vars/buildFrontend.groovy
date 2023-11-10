def call(REGISTRY_DOCKER, BUIDL_CONTAINER_NAME, DOCKER_TAG, MAIL_SEND_TO, TELEGRAM_BOT_TOKEN, TELEGRAM_CHAT_ID) {
    cleanDockerImages(REGISTRY_DOCKER, BUIDL_CONTAINER_NAME, DOCKER_TAG)
    
    try {
        buildDockerImage(REGISTRY_DOCKER, BUIDL_CONTAINER_NAME, DOCKER_TAG)
        sendTelegramMessage("Docker build Successfully!")
    } catch (Exception e) {
        echo "Build failed, retrying..."
        cleanDockerImages(REGISTRY_DOCKER, BUIDL_CONTAINER_NAME, DOCKER_TAG)
        buildDockerImage(REGISTRY_DOCKER, BUIDL_CONTAINER_NAME, DOCKER_TAG)
        sendTelegramMessage("Docker build failed!")
        throw e
    }
}

def cleanDockerImages(REGISTRY_DOCKER, BUIDL_CONTAINER_NAME, DOCKER_TAG) {
    sh """
        docker rmi -f ${BUIDL_CONTAINER_NAME}:${DOCKER_TAG}
        docker rmi -f ${REGISTRY_DOCKER}/${BUIDL_CONTAINER_NAME}:${DOCKER_TAG}
    """
}

def buildDockerImage(REGISTRY_DOCKER, BUIDL_CONTAINER_NAME, DOCKER_TAG) {
    sh "docker build -t ${BUIDL_CONTAINER_NAME}:${DOCKER_TAG} -t ${REGISTRY_DOCKER}/${BUIDL_CONTAINER_NAME}:${DOCKER_TAG} ."
}

def sendTelegramMessage(message) {
    sh "curl -s -X POST https://api.telegram.org/bot${TELEGRAM_BOT_TOKEN}/sendMessage -d chat_id=${TELEGRAM_CHAT_ID} -d text='${message}'"
}

def sendGmailMessage(message) {
    mail bcc: '', body: message, cc: '', from: '', replyTo: '', subject: 'Docker Build Status', to: MAIL_SEND_TO  
}

// def dockerfileContent = '''
// # Dockerfile
// FROM node:14 as build
// WORKDIR /app
// COPY ./ ./
// RUN npm install --force
// RUN npm run build

// FROM nginx:1.23.2
// COPY --from=build /app/build /usr/share/nginx/html

// EXPOSE 80
// CMD ["nginx", "-g", "daemon off;"]
// '''
