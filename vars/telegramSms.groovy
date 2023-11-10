def sendTelegramMessage(Map<String, Object> envVars, String message) {
    def telegramBotToken = envVars.TELEGRAM_BOT_TOKEN
    def telegramChatId = envVars.TELEGRAM_CHAT_ID

    // Use the environment variables and message as needed
    sh """
    curl -s -X POST https://api.telegram.org/bot${telegramBotToken}/sendMessage -d chat_id=${telegramChatId} -d text='${message}'
    """
}