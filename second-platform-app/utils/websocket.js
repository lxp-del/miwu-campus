// utils/websocket.js
class WebSocketManager {
    constructor() {
        this.socket = null
        this.reconnectCount = 0
        this.maxReconnectCount = 5
        this.reconnectDelay = 3000
        this.messageHandlers = new Map()
        this.isConnecting = false
        this.userId = null
        this.onOpenCallback = null
        this.onCloseCallback = null
        this.onErrorCallback = null
        this.onMessageCallback = null
        this.heartbeatInterval = null
        this.isManualClose = false // 是否手动关闭
    }

    // 连接WebSocket
    connect(userId, onMessage, onOpen, onClose, onError) {
        if (this.isConnecting || (this.socket && this.isSocketOpen())) {
            console.log('WebSocket already connected or connecting')
            return
        }

        this.userId = userId
        this.onMessageCallback = onMessage
        this.onOpenCallback = onOpen
        this.onCloseCallback = onClose
        this.onErrorCallback = onError
        this.isManualClose = false

        // 获取WebSocket服务器地址
        const baseUrl = this.getWebSocketUrl()
        const wsUrl = `${baseUrl}/ws/chat/${userId}`
        
        console.log('Connecting to WebSocket:', wsUrl)
        this.isConnecting = true

        // #ifdef MP-WEIXIN
        this.connectInMp(wsUrl)
        // #endif

        // #ifdef H5
        this.connectInH5(wsUrl)
        // #endif

        // #ifdef APP-PLUS
        this.connectInApp(wsUrl)
        // #endif
    }

    // 小程序环境连接
    // #ifdef MP-WEIXIN
    connectInMp(wsUrl) {
        this.socket = uni.connectSocket({
            url: wsUrl,
            success: () => {
                console.log('WebSocket connecting...')
            },
            fail: (err) => {
                console.error('WebSocket connection failed', err)
                this.isConnecting = false
                if (this.onErrorCallback) this.onErrorCallback(err)
                this.reconnect()
            }
        })

        this.socket.onOpen(() => {
            console.log('WebSocket connected')
            this.isConnecting = false
            this.reconnectCount = 0
            this.startHeartbeat()
            if (this.onOpenCallback) this.onOpenCallback()
        })

        this.socket.onMessage((res) => {
            try {
                const data = JSON.parse(res.data)
                this.handleMessage(data)
            } catch (e) {
                console.error('Parse message error', e)
            }
        })

        this.socket.onClose(() => {
            console.log('WebSocket closed')
            this.stopHeartbeat()
            this.isConnecting = false
            if (this.onCloseCallback) this.onCloseCallback()
            if (!this.isManualClose) {
                this.reconnect()
            }
        })

        this.socket.onError((err) => {
            console.error('WebSocket error', err)
            this.isConnecting = false
            if (this.onErrorCallback) this.onErrorCallback(err)
        })
    }
    // #endif

    // H5环境连接
    // #ifdef H5
    connectInH5(wsUrl) {
        this.socket = new WebSocket(wsUrl)

        this.socket.onopen = () => {
            console.log('WebSocket connected')
            this.isConnecting = false
            this.reconnectCount = 0
            this.startHeartbeat()
            if (this.onOpenCallback) this.onOpenCallback()
        }

        this.socket.onmessage = (event) => {
            try {
                const data = JSON.parse(event.data)
                this.handleMessage(data)
            } catch (e) {
                console.error('Parse message error', e)
            }
        }

        this.socket.onclose = () => {
            console.log('WebSocket closed')
            this.stopHeartbeat()
            this.isConnecting = false
            if (this.onCloseCallback) this.onCloseCallback()
            if (!this.isManualClose) {
                this.reconnect()
            }
        }

        this.socket.onerror = (err) => {
            console.error('WebSocket error', err)
            this.isConnecting = false
            if (this.onErrorCallback) this.onErrorCallback(err)
        }
    }
    // #endif

    // App环境连接
    // #ifdef APP-PLUS
    connectInApp(wsUrl) {
        this.socket = new plus.websocket(wsUrl)

        this.socket.onopen = () => {
            console.log('WebSocket connected')
            this.isConnecting = false
            this.reconnectCount = 0
            this.startHeartbeat()
            if (this.onOpenCallback) this.onOpenCallback()
        }

        this.socket.onmessage = (event) => {
            try {
                const data = JSON.parse(event.data)
                this.handleMessage(data)
            } catch (e) {
                console.error('Parse message error', e)
            }
        }

        this.socket.onclose = () => {
            console.log('WebSocket closed')
            this.stopHeartbeat()
            this.isConnecting = false
            if (this.onCloseCallback) this.onCloseCallback()
            if (!this.isManualClose) {
                this.reconnect()
            }
        }

        this.socket.onerror = (err) => {
            console.error('WebSocket error', err)
            this.isConnecting = false
            if (this.onErrorCallback) this.onErrorCallback(err)
        }
    }
    // #endif

    // 处理接收到的消息
    handleMessage(data) {
        // 处理心跳响应
        if (data.type === 'pong') {
            return
        }
        
        // 调用全局消息回调
        if (this.onMessageCallback) {
            this.onMessageCallback(data)
        }
        
        // 调用注册的消息处理器
        if (data.type && this.messageHandlers.has(data.type)) {
            const handlers = this.messageHandlers.get(data.type)
            handlers.forEach(handler => handler(data))
        }
    }

    // 检查socket是否打开
    isSocketOpen() {
        // #ifdef MP-WEIXIN
        return this.socket && this.socket.readyState === WebSocket.OPEN
        // #endif
        
        // #ifdef H5
        return this.socket && this.socket.readyState === WebSocket.OPEN
        // #endif
        
        // #ifdef APP-PLUS
        return this.socket && this.socket.readyState === 1
        // #endif
    }

    // 重连
    reconnect() {
        if (this.reconnectCount >= this.maxReconnectCount) {
            console.log('Max reconnect attempts reached')
            return
        }
        
        if (this.isManualClose) {
            console.log('Manual close, skip reconnect')
            return
        }

        setTimeout(() => {
            console.log(`Reconnecting... attempt ${this.reconnectCount + 1}`)
            this.reconnectCount++
            this.connect(this.userId, this.onMessageCallback, this.onOpenCallback, this.onCloseCallback, this.onErrorCallback)
        }, this.reconnectDelay)
    }

    // 发送消息
    send(message) {
        if (!this.isSocketOpen()) {
            console.error('WebSocket is not connected')
            return false
        }

        const messageStr = typeof message === 'string' ? message : JSON.stringify(message)
        
        // #ifdef MP-WEIXIN
        this.socket.send({
            data: messageStr,
            success: () => {
                console.log('Message sent')
            },
            fail: (err) => {
                console.error('Send message failed', err)
            }
        })
        // #endif
        
        // #ifdef H5
        this.socket.send(messageStr)
        // #endif
        
        // #ifdef APP-PLUS
        this.socket.send(messageStr)
        // #endif
        
        return true
    }

    // 发送聊天消息
    sendChatMessage(sessionId, goodsId, fromUserId, toUserId, messageType, content, goodsInfo = null) {
        const message = {
            type: 'chat',
            data: {
                sessionId: sessionId,
                goodsId: goodsId,
                fromUserId: fromUserId,
                toUserId: toUserId,
                messageType: messageType,
                content: content,
                goodsInfo: goodsInfo ? JSON.stringify(goodsInfo) : null,
                createTime: new Date().toISOString()
            }
        }
        return this.send(message)
    }

    // 发送已读回执
    sendReadReceipt(sessionId, userId) {
        const message = {
            type: 'read',
            sessionId: sessionId,
            userId: userId
        }
        return this.send(message)
    }

    // 发送心跳
    sendHeartbeat() {
        const message = {
            type: 'ping'
        }
        return this.send(message)
    }

    // 开始心跳
    startHeartbeat() {
        this.stopHeartbeat()
        this.heartbeatInterval = setInterval(() => {
            if (this.isSocketOpen()) {
                this.sendHeartbeat()
            }
        }, 30000) // 每30秒发送一次心跳
    }

    // 停止心跳
    stopHeartbeat() {
        if (this.heartbeatInterval) {
            clearInterval(this.heartbeatInterval)
            this.heartbeatInterval = null
        }
    }

    // 注册消息处理器
    onMessageType(type, handler) {
        if (!this.messageHandlers.has(type)) {
            this.messageHandlers.set(type, new Set())
        }
        this.messageHandlers.get(type).add(handler)
    }

    // 移除消息处理器
    offMessageType(type, handler) {
        if (this.messageHandlers.has(type)) {
            if (handler) {
                this.messageHandlers.get(type).delete(handler)
            } else {
                this.messageHandlers.delete(type)
            }
        }
    }

    // 移除所有消息处理器
    offAllMessageTypes() {
        this.messageHandlers.clear()
    }

    // 获取WebSocket服务器地址
    getWebSocketUrl() {
        // 开发环境
        if (process.env.NODE_ENV === 'development') {
            // #ifdef H5
            return 'ws://localhost:8080' // 替换为你的后端地址
            // #endif
            // #ifdef MP-WEIXIN
            return 'wss://your-domain.com' // 小程序必须使用wss
            // #endif
        }
        
        // 生产环境
        return 'wss://your-domain.com' // 替换为你的实际域名
    }

    // 关闭连接
    close() {
        this.isManualClose = true
        this.stopHeartbeat()
        
        if (this.socket) {
            // #ifdef MP-WEIXIN
            this.socket.close({
                success: () => {
                    console.log('WebSocket closed manually')
                }
            })
            // #endif
            
            // #ifdef H5
            this.socket.close()
            // #endif
            
            // #ifdef APP-PLUS
            this.socket.close()
            // #endif
            
            this.socket = null
        }
        
        this.isConnecting = false
        this.userId = null
        this.offAllMessageTypes()
    }

    // 获取连接状态
    getConnectionStatus() {
        if (!this.socket) return 'disconnected'
        // #ifdef MP-WEIXIN
        if (this.socket.readyState === WebSocket.CONNECTING) return 'connecting'
        if (this.socket.readyState === WebSocket.OPEN) return 'connected'
        if (this.socket.readyState === WebSocket.CLOSING) return 'closing'
        return 'disconnected'
        // #endif
        
        // #ifdef H5
        if (this.socket.readyState === WebSocket.CONNECTING) return 'connecting'
        if (this.socket.readyState === WebSocket.OPEN) return 'connected'
        if (this.socket.readyState === WebSocket.CLOSING) return 'closing'
        return 'disconnected'
        // #endif
        
        // #ifdef APP-PLUS
        return this.socket.readyState === 1 ? 'connected' : 'disconnected'
        // #endif
    }
}

// 创建单例
const websocketManager = new WebSocketManager()

export default websocketManager