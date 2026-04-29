# JavaFX Project: Chat Application

## API URL
https://chatty.brys.me/

## How it works
- API
  - Authenticate Clients (Think basic Password + Salt SHA512 or HMAC) uses token for requests.
  - REST 
  - Create messages
  - Delete Messages
  - Send Messages (With Multimedia?)
  - WebSocket (Client connects via websocket to receive messages in real time.)
- Client
  - Connects to central server
  - Receives messages via websocket
  - Sends messages via HTTP API
  - Saves token to local db
